/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

import com.bittheory.jaxrs.sitechecker.domain.SiteResponse;
import com.bittheory.jaxrs.sitechecker.jenkins.Build;
import com.bittheory.jaxrs.sitechecker.jenkins.BuildContext;
import javax.annotation.PostConstruct;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * REST Web Service
 *
 * @author nstuart1
 */
@Path("builds/{buildName}")
@RequestScoped
public class JenkinsBuildResource {
    
    @Context
    private UriInfo context;
    private String JENKINS_URL;
    @Inject
    private BuildContext buildContext;
    @Inject
    private URLReader reader;
    
    @PostConstruct
    public void init(){
        JENKINS_URL = System.getProperty("sitechecker.jenkins.url");
    }

    /**
     * Creates a new instance of JenkinsBuildResource
     */
    public JenkinsBuildResource() {
    }

    /**
     * Retrieves representation of an instance of com.bittheory.jaxrs.sitechecker.JenkinsBuildResource
     * @return an instance of com.bittheory.jaxrs.sitechecker.jenkins.Build
     */
    @GET
    @Produces("application/json")
    @Path("/last")
    public Build getLastBuildStatus(@PathParam("buildName") String buildName) {
        SiteResponse urlResp = new SiteResponse();// reader.getUrl(JENKINS_URL + buildName + "/lastBuild/api/json");
        Build bld = buildContext.unmarshal(urlResp.getResponse());
        return bld;
    }
}
