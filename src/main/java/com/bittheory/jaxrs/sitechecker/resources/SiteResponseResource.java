/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker.resources;

import com.bittheory.jaxrs.sitechecker.SiteManager;
import com.bittheory.jaxrs.sitechecker.URLReader;
import com.bittheory.jaxrs.sitechecker.domain.Site;
import com.bittheory.jaxrs.sitechecker.domain.SiteResponse;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

/**
 * REST Web Service
 *
 * @author nick
 */
@Path("site/{siteId}/response")
@RequestScoped
public class SiteResponseResource {

    @Context
    private UriInfo context;
    @Inject
    private URLReader reader;
    @Inject
    private SiteManager siteManager;

    /**
     * Creates a new instance of SiteCheckResource
     */
    public SiteResponseResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.bittheory.jaxrs.sitechecker.SiteCheckResource
     *
     * @return an instance of com.bittheory.jaxrs.sitechecker.Site
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/latest")
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
    public List<SiteResponse> list(@PathParam("siteId") Long id, @QueryParam("start") Long start) {
        if (start == null) {
            return siteManager.load(id).getResponses();
        } else {
            return siteManager.responsesFrom(siteManager.load(id), start);
        }
    }
}
