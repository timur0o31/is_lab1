package ru.itmo.tim.controller;

import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ru.itmo.tim.exception.DomainException;
import ru.itmo.tim.service.WorkerService;
import ru.itmo.tim.enums.Position;
import ru.itmo.tim.requestDto.WorkerRequestDto;
import ru.itmo.tim.responseDto.WorkerResponseDto;
import ru.itmo.tim.websocket.WebSocket;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/workers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WorkerController {

    @Inject
    private WorkerService workerService;

    @POST
    public Response createWorker(@Valid WorkerRequestDto dto) {
        try {
            WorkerResponseDto worker = workerService.createWorker(dto);
            WebSocket.broadcast("worker");
            return Response.ok(worker).build();
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException cause) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", cause.getMessage()))
                        .build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getWorker(@PathParam("id") int id) {
        WorkerResponseDto worker = workerService.getWorker(id);
        return Response.ok(worker).build();
    }
    @GET
    public Response getAllWorkers(@QueryParam("page") @DefaultValue("1") int page,
                                  @QueryParam("size") @DefaultValue("10") int size,
                                  @QueryParam("sortColumn") String sortColumn,
                                  @QueryParam("asc") @DefaultValue("true") boolean asc,
                                  @QueryParam("name") String name,
                                  @QueryParam("position") String position,
                                  @QueryParam("salary") String salary,
                                  @QueryParam("rating") String rating) {
        Map<String, Object> filters = new HashMap<>();
        List<String> invalid = new ArrayList<>();
        if (name != null && !name.isEmpty())filters.put("name", name);
        if (salary != null){
            try{
                filters.put("salary", Float.parseFloat(salary));
            }catch (NumberFormatException e){
                invalid.add("salary");
            }
        }
        if (rating != null){
            try {
                filters.put("rating", Integer.parseInt(rating));
            }catch(NumberFormatException e){
                invalid.add("rating");
            }
        }
        if (position != null && !position.isEmpty()) filters.put("position", Position.valueOf(position));
        if (!invalid.isEmpty()) return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Некорректные фильтры", "invalidFields", invalid))
                .build();

        List<WorkerResponseDto> workers = workerService.getAll(page, size, filters, sortColumn, asc);
        Long count = workerService.getCount(filters);

        return Response.ok(Map.of("content",workers,"totalRecords",count)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateWorker(@PathParam("id") int id,
                                 @Valid WorkerRequestDto dto) {
        try {
            WorkerResponseDto updated = workerService.updateWorker(id, dto);
            WebSocket.broadcast("worker");
            return Response.ok(updated).build();
        } catch( IllegalArgumentException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error",e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteWorker(@PathParam("id") int id) {
        workerService.deleteWorker(id);
        WebSocket.broadcast("worker");
        return Response.noContent().build();
    }


    @GET
    @Path("/sum-rating")
    public Double getSumRating() {
        return workerService.sumRating();
    }

    @GET
    @Path("/name-prefix")
    public Response getWorkersByNamePrefix(@QueryParam("prefix") String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Parameter 'prefix' must not be empty"))
                    .build();
        }
        List<WorkerResponseDto> result = workerService.findByNamePrefix(prefix);

        return Response.ok(result).build();
    }

    @GET
    @Path("/end-date")
    public Response getWorkersByEndDate(@QueryParam("date") String date) {
        if (date == null || date.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Parameter 'prefix' must not be empty"))
                    .build();
        }
        try {
            LocalDate newDate = LocalDate.parse(date);
            List<WorkerResponseDto> result = workerService.findByEndDateAfter(newDate);
            return Response.ok(result).build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format, expected YYYY-MM-DD").build();
        }

    }

    @GET
    @Path("/unemployed")
    public Response getUnemployedPersons() {
        List<WorkerResponseDto> persons = workerService.getUnemployedWorkers();
        return Response.ok(persons).build();
    }

    @PUT
    @Path("/hire")
    public Response hireWorker(@QueryParam("workerId") int workerId,
                               @QueryParam("organizationId") Long orgId) {
        workerService.hireWorker(workerId, orgId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/fire/")
    public Response fireWorker(@PathParam("id") int workerId) {
        workerService.fireWorker(workerId);
        return Response.ok().build();
    }

    @GET
    @Path("/by-organization/{orgId}")
    public Response getWorkersByOrganization(@PathParam("orgId") Long orgId) {
        List<WorkerResponseDto> result = workerService.findByOrganization(orgId);
        return Response.ok(result).build();
    }
}
