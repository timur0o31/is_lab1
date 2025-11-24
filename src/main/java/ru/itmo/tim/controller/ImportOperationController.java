package ru.itmo.tim.controller;

import ru.itmo.tim.requestDto.ImportOperationRequestDto;
import ru.itmo.tim.responseDto.ImportOperationResponseDto;
import ru.itmo.tim.service.ImportOperationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.util.HashMap;
import java.util.Map;

@Path("/imports")
@Produces(MediaType.APPLICATION_JSON)
public class ImportOperationController {
    @Inject
    private ImportOperationService importOperationService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importWorkers(@MultipartForm ImportOperationRequestDto form){
        try{
            ImportOperationResponseDto importOperation = importOperationService.importWorkers(form);
            Map<String, Object> body = new HashMap<>();
            body.put("status", importOperation.getStatus());
            body.put("addedCount", importOperation.getCount());
            return Response.ok(body).build();
        }catch(Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @GET
    public Response getAllOperations(){
        return Response.ok().build();
    }
}
