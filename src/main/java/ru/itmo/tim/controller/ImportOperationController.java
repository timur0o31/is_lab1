package ru.itmo.tim.controller;

import ru.itmo.tim.service.ImportOperationService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/imports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImportOperationController {
    @Inject
    private ImportOperationService importOperationService;

    @GET
    public Response getAllOperations(){
        return Response.ok().build();
    }
}
