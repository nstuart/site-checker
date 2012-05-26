/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker.resources;

import com.bittheory.jaxrs.sitechecker.SiteManager;
import com.bittheory.jaxrs.sitechecker.URLReader;
import com.bittheory.jaxrs.sitechecker.domain.Site;
import com.bittheory.jaxrs.sitechecker.domain.SiteResponse;
import com.sun.jersey.api.json.JSONJAXBContext;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author nick
 */
@Path("site")
@RequestScoped
public class SiteResource {

    @Context
    private UriInfo context;
    @Inject
    private URLReader reader;
    @Inject
    private SiteManager siteManager;
    private JSONJAXBContext jAXBContext;

    /**
     * Creates a new instance of SiteCheckResource
     */
    public SiteResource() {
        try {

            jAXBContext = new JSONJAXBContext(Site.class);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retrieves representation of an instance of
     * com.bittheory.jaxrs.sitechecker.SiteCheckResource
     *
     * @return an instance of com.bittheory.jaxrs.sitechecker.Site
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{siteId}/response/latest")
    public SiteResponse getJson(@PathParam("siteId") Long id) {
        Site site = siteManager.load(id);
        if (site.getResponses().isEmpty()) {
            return null;
        } else {
            return site.getResponses().get(site.getResponses().size() - 1);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{siteId}")
    public Site getSite(@PathParam("siteId") Long id) {
        Site site = siteManager.load(id);
        return site;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String list() throws JAXBException, JSONException {
        JSONArray obj = new JSONArray();
        List<Site> all = siteManager.getAll();
        for (Site site : all) {
            obj.put(siteToJson(site));
        }
        return obj.toString();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSite(Site siteDef) throws JAXBException {
        Site site = siteManager.create(siteDef);
        return Response.created(getSiteUri(site.getId())).build();
    }

    @DELETE
    @Path("{siteId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteSite(@PathParam("siteId") Long id) throws JAXBException {
        siteManager.delete(id);
        return Response.noContent().build();
    }

    private URI getSiteUri(long siteId) {
        try {
            return new URI("http", null, "localhost", 8080, "/site-checker/resources/site/" + siteId, null, null);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JSONObject siteToJson(Site site) throws JAXBException, JSONException {
        StringWriter writer = new StringWriter();
        jAXBContext.createJSONMarshaller().marshallToJSON(site, writer);
        JSONObject json = new JSONObject(writer.toString());
        String siteUri = getSiteUri(site.getId()).toString();
        json.put("latest_url", siteUri + "/response/latest");
        json.put("responses_url", siteUri + "/response");
        return json;
    }
}
