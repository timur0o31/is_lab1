package ru.itmo.tim.controller;

import ru.itmo.tim.cache.CacheManager;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

@Path("/cache")
@Produces(MediaType.APPLICATION_JSON)
public class CacheController {

    @Inject
    private CacheManager cacheStatisticsManager;

    @GET
    @Path("/statistics/status")
    public Response getStatisticsStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("statisticsLoggingEnabled", cacheStatisticsManager.isStatisticsLoggingEnabled());
        return Response.ok(response).build();
    }

    @POST
    @Path("/statistics/enable")
    public Response enableStatistics() {
        cacheStatisticsManager.enableStatisticsLogging();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hibernate statistics logging enabled");
        response.put("status", "enabled");
        response.put("timestamp", System.currentTimeMillis());

        return Response.ok(response).build();
    }

    @POST
    @Path("/statistics/disable")
    public Response disableStatistics() {
        cacheStatisticsManager.disableStatisticsLogging();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hibernate statistics logging disabled");
        response.put("status", "disabled");
        response.put("timestamp", System.currentTimeMillis());

        return Response.ok(response).build();
    }
}
