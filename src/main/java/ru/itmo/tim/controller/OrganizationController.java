package ru.itmo.tim.controller;

import ru.itmo.tim.service.OrganizationService;
import ru.itmo.tim.requestDto.OrganizationRequestDto;
import ru.itmo.tim.responseDto.OrganizationResponseDto;
import ru.itmo.tim.websocket.WebSocket;

import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrganizationController {
    @Inject
    private OrganizationService organizationService;
    @POST
    public Response addOrganization(@Valid OrganizationRequestDto dto) {
        try {
            OrganizationResponseDto organization = organizationService.createOrganization(dto);
            WebSocket.broadcast("organization");
            return Response.ok(organization).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Validation failed", "details", e.getMessage()))
                    .build();
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException cause) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", cause.getMessage()))
                        .build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }catch(NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error",e)).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getOrganization(@PathParam("id") Long id) {
        try {
            OrganizationResponseDto organization = organizationService.getOrganization(id);
            return Response.ok(organization).build();
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException cause) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", cause.getMessage()))
                        .build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getAllOrganizations(@QueryParam("page") int page,
                                      @QueryParam("size") int size,
                                      @QueryParam("sortColumn") String sortColumn,
                                      @QueryParam("asc") @DefaultValue("true") boolean asc,
                                      @QueryParam("annualTurnover") String annualTurnover,
                                      @QueryParam("employeesCount") String employeesCount,
                                      @QueryParam("fullName") String fullName,
                                      @QueryParam("rating") String rating) {
        Map<String, Object> filters = new HashMap<>();
        List<String> invalid = new ArrayList<>();
        if (annualTurnover != null){
            try{
                filters.put("annualTurnover", Float.parseFloat(annualTurnover));
            }catch (NumberFormatException e){
                invalid.add("annualTurnover");
            }
        }
        if (employeesCount != null){
            try{
                filters.put("employeesCount", Long.parseLong(employeesCount));
            }catch (NumberFormatException e){
                invalid.add("employeesCount");
            }
        }
        if (fullName != null && !fullName.isEmpty())filters.put("fullName", fullName);
        if (rating != null){
            try{
                filters.put("rating", Float.parseFloat(rating));
            }catch (NumberFormatException e){
                invalid.add("rating");
            }
        }
        if (!invalid.isEmpty()) return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Некорректные фильтры", "invalidFields", invalid))
                .build();
        List<OrganizationResponseDto> organizations = organizationService.getAllOrganizations(page, size,sortColumn,asc,filters);
        Long count = organizationService.getCount(filters);
        return Response.ok(Map.of("content",organizations,"totalRecords",count)).build();
    }
    @PUT
    @Path("/{id}")
    public Response updateOrganization(@PathParam("id") Long id, @Valid OrganizationRequestDto dto) {
        try {
            OrganizationResponseDto organization = organizationService.updateOrganization(id, dto);
            WebSocket.broadcast("organization");
            return Response.ok(organization).build();
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException cause) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", cause.getMessage()))
                        .build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }catch(NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error",e)).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrganizationWithWorkers(@PathParam("id") Long id, @QueryParam("newOrgId") Long newId) {
        organizationService.deleteOrganizationWithWorkers(id, newId);
        WebSocket.broadcast("organization");
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/countWorkers/{id}")
    public Response getCountWorkersByOrganization(@PathParam("id") Long orgId) {
        Long result = organizationService.countWorkers(orgId);
        return Response.ok(Map.of("count", result)).build();
    }

    @GET
    @Path("/orgToTransfer/{id}")
    public Response getOrganizationsForTransfer(@PathParam("id") Long excludeId) {
        List<OrganizationResponseDto> organizations = organizationService.getOtherOrganizations(excludeId);
        return Response.ok(organizations).build();
    }

}