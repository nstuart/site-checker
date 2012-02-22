/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author nick
 */
@Path("sitecheck")
@RequestScoped
public class SiteCheckResource {

    @Context
    private UriInfo context;
    @Inject
    private URLReader reader;

    /**
     * Creates a new instance of SiteCheckResource
     */
    public SiteCheckResource() {
    }

    /**
     * Retrieves representation of an instance of com.bittheory.jaxrs.sitechecker.SiteCheckResource
     * @return an instance of com.bittheory.jaxrs.sitechecker.Site
     */
    @GET
    @Produces("application/json")
    public SiteResponse getJson(@QueryParam("url") String url) {
        return reader.getUrl(url);
    }

}