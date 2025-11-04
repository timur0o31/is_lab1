package app.controller;

import app.service.PersonService;

import app.Color;
import app.Country;
import app.requestDto.PersonRequestDto;
import app.responseDto.PersonResponseDto;
import app.websocket.WebSocket;
import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PersonController {
    @Inject
    private PersonService personService;
    @POST
    public Response addPerson(@Valid PersonRequestDto dto) {
        try {
            PersonResponseDto person = personService.addPerson(dto);
            WebSocket.broadcast("person");
            return Response.ok(person).build();
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
    public Response getPerson(@PathParam("id") Long id) {
        try {
            PersonResponseDto person = personService.getPerson(id);
            return Response.ok(person).build();
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
    @Path("/all")
    public Response getAllPersons(@QueryParam("page") int page,
                                  @QueryParam("size") int size,
                                  @QueryParam("sortColumn") String sortColumn,
                                  @QueryParam("asc") @DefaultValue("true") boolean asc,
                                  @QueryParam("passportId") String passportId,
                                  @QueryParam("eyeColor") String eyeColor,
                                  @QueryParam("hairColor") String hairColor,
                                  @QueryParam("nationality") String nationality) {
        Map<String, Object> filters = new HashMap<>();
        List<String> invalid = new ArrayList<>();
        if (passportId != null){
            try{
                filters.put("passportId", Long.parseLong(passportId));
            }catch (NumberFormatException e){
                invalid.add("passportId");
            }
        }
        if (eyeColor != null && !eyeColor.isEmpty()) filters.put("eyeColor", Color.valueOf(eyeColor));
        if (hairColor != null && !hairColor.isEmpty()) filters.put("hairColor", Color.valueOf(hairColor));
        if (nationality != null && !nationality.isEmpty()) filters.put("nationality", Country.valueOf(nationality));
        if (!invalid.isEmpty()) return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Некорректные фильтры", "invalidFields", invalid))
                .build();

        List<PersonResponseDto> persons = personService.getAllPersons(page, size, sortColumn, asc, filters);
        Long count = personService.getCount(filters);
        return Response.ok(Map.of("content",persons,"totalRecords",count)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") Long id, @Valid PersonRequestDto dto) {
        try {
            PersonResponseDto person = personService.updatePerson(id, dto);
            WebSocket.broadcast("person");
            return Response.ok(person).build();
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

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        try {
            personService.deletePerson(id);
            WebSocket.broadcast("person");
            return Response.status(Response.Status.OK).build();
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
}
