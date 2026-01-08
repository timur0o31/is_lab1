package ru.itmo.tim.controller;

import jdk.jshell.Snippet;
import ru.itmo.tim.enums.Status;
import ru.itmo.tim.requestDto.ImportOperationRequestDto;
import ru.itmo.tim.responseDto.ImportOperationResponseDto;
import ru.itmo.tim.service.ImportOperationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<ImportOperationResponseDto> importOperation = importOperationService.getAllImportOperations();
        return Response.ok(Map.of("content",importOperation, "totalRecords", importOperation.size())).build();
    }
    @GET

    public Response getAllOpertions(@QueryParam("page") int page,
                                    @QueryParam("size") int size,
                                    @QueryParam("sortColumn") String sortColumn,
                                    @QueryParam("asc") @DefaultValue("true") boolean asc,
                                    @QueryParam("id") String id,
                                    @QueryParam("count") String count,
                                    @QueryParam("status") String status
                                    ) {
        Map<String, Object> filters = new HashMap<>();
        List<String> invalid = new ArrayList<>();
        if (!id.isBlank()){
            try{
                filters.put("id",Integer.parseInt(id));
            }catch(NumberFormatException e){
                invalid.add(id);
            }
        }
        if (!count.isBlank()){
            try{
                filters.put("count", Integer.parseInt(count));
            }catch(NumberFormatException e){
                invalid.add(count);
            }
        }
        if (!status.isBlank()) filters.put(status, Status.valueOf(status));
        if (!invalid.isEmpty()) return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Некорректные фильтры", "invalidFields", invalid))
                .build();
        List<ImportOperationResponseDto> importOperations = importOperationService.getAllImportOperations(page,size,sortColumn,asc,filters);
        return Response.ok(Map.of("content", importOperations)).build();
    }
}
