package ru.itmo.tim.controller;

import ru.itmo.tim.service.PersonService;

import ru.itmo.tim.enums.Color;
import ru.itmo.tim.enums.Country;
import ru.itmo.tim.requestDto.PersonRequestDto;
import ru.itmo.tim.responseDto.PersonResponseDto;
import ru.itmo.tim.websocket.WebSocket;

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

@Path("/persons")
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
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
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
        } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", e.getMessage()))
                        .build();
        }
    }
    @GET
    public Response getAllPersons(@QueryParam("page") int page,
                                  @QueryParam("size") int size,
                                  @QueryParam("sortColumn") String sortColumn,
                                  @QueryParam("asc") @DefaultValue("true") boolean asc,
                                  @QueryParam("id") String id,
                                  @QueryParam("passportId") String passportId,
                                  @QueryParam("eyeColor") String eyeColor,
                                  @QueryParam("hairColor") String hairColor,
                                  @QueryParam("nationality") String nationality) {
        Map<String, Object> filters = new HashMap<>();
        List<String> invalid = new ArrayList<>();
        if (id != null && !id.isEmpty()){
            try{
                filters.put("annualTurnover", Long.parseLong(id));
            }catch (NumberFormatException e){
                invalid.add("id");
            }
        }
        if (passportId != null && !passportId.isEmpty()) filters.put("passportId", passportId);
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
        } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST)
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
        } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", e.getMessage()))
                        .build();
        }
    }
}
