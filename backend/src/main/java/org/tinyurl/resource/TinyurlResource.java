package org.tinyurl.resource;

import com.fasterxml.jackson.databind.JsonNode;
import org.tinyurl.response.TinyurlData;
import org.tinyurl.service.ITinyurlService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tinyurl")
public class TinyurlResource {
    ITinyurlService tinyurlService;

    public TinyurlResource(ITinyurlService tinyurlService) {
        this.tinyurlService = tinyurlService;
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(TinyurlData data) {
        String tinyUrl;
        try {
            tinyUrl = tinyurlService.shorten(data.getLongUrl());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(tinyUrl).build();
    }

    @GET
    @Path("/{alias}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("alias") String alias) {
        String longUrl;
        try {
            String shortUrl = "http://localhost:8080/tinyurl/" + alias;
            longUrl = tinyurlService.get(shortUrl);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.seeOther(java.net.URI.create(longUrl)).build();
    }

    @DELETE
    @Path("/{alias}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("alias") String alias) {
        try {
            String shortUrl = "http://localhost:8080/tinyurl/" + alias;
            tinyurlService.deleteUrl(shortUrl);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity("Successfully deleted").build();
    }

    @GET
    @Path("/getAll")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TinyurlData> tinyurlDataList;
        try {
            tinyurlDataList = tinyurlService.getAll();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(tinyurlDataList).build();
    }
}
