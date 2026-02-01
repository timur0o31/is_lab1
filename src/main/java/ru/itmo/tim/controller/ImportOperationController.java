package ru.itmo.tim.controller;

import ru.itmo.tim.entity.ImportOperation;
import ru.itmo.tim.entity.Worker;
import ru.itmo.tim.enums.Status;
import ru.itmo.tim.requestDto.ImportOperationRequestDto;
import ru.itmo.tim.responseDto.ImportOperationResponseDto;
import ru.itmo.tim.service.ImportOperationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import ru.itmo.tim.websocket.WebSocket;

import java.io.InputStream;
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
            WebSocket.broadcast("importOperation");
            return Response.ok(importOperation).build();
        }catch(Exception e){
            WebSocket.broadcast("importOperation");
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        }
    }
    @GET
    public Response getAllOpertions(@QueryParam("page") int page,
                                    @QueryParam("size") int size,
                                    @QueryParam("sortColumn") String sortColumn,
                                    @QueryParam("asc") @DefaultValue("true") boolean asc,
                                    @QueryParam("id") String id,
                                    @QueryParam("count") String count,
                                    @QueryParam("status") String status,
                                    @QueryParam("message") String message
                                    ) {
        Map<String, Object> filters = new HashMap<>();
        List<String> invalid = new ArrayList<>();
        if (id != null){
            try{
                filters.put("id",Long.parseLong(id));
            }catch(NumberFormatException e){
                invalid.add(id);
            }
        }
        if (count != null){
            try{
                filters.put("count", Long.parseLong(count));
            }catch(NumberFormatException e){
                invalid.add(count);
            }
        }
        if (status != null) filters.put("status", Status.valueOf(status));
        if (message != null && !message.isEmpty())filters.put("message", message);
        if (!invalid.isEmpty()) return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Некорректные фильтры", "invalidFields", invalid))
                .build();
        List<ImportOperationResponseDto> importOperations = importOperationService.getAllImportOperations(page,size,sortColumn,asc,filters);
        Long totalRecords = importOperationService.getCount(filters);
        return Response.ok(Map.of("content", importOperations, "totalRecords", totalRecords)).build();
    }
    @GET
    @Path("/{id}/download")
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
    public Response downloadFile(@PathParam("id") Long importId){
        try{
            InputStream fileStream = importOperationService.downloadImportFile(importId);
            ImportOperationResponseDto importOperation = importOperationService.getImportOperationById(importId);
            return Response.ok(fileStream)
                    .header("Content-Disposition", "attachment; filename=\"" + importOperation.getFileName() + "\"")
                    .build();
        }catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            String shortMessage;
            if (errorMessage != null && errorMessage.contains("MinIO")) {
                shortMessage = "MinIO storage is currently unavailable";
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity(Map.of("error", shortMessage))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            } else {
                shortMessage = errorMessage != null ? errorMessage : "File not found";
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", shortMessage))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
