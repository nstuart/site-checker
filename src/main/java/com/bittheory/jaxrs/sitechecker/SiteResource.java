/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

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
@Path("site")
@RequestScoped
public class SiteResource {

    @Context
    private UriInfo context;
    @Inject
    private URLReader reader;
    @Inject
    private SiteManager siteManager;

    /**
     * Creates a new instance of SiteCheckResource
     */
    public SiteResource() {
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
    public List<Site> list() {
        return siteManager.getAll();
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Site addSite(Site siteDef) throws JAXBException{
        return siteManager.create(siteDef);
    }
}
