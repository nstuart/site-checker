/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

import com.bittheory.jaxrs.sitechecker.domain.Site;
import com.bittheory.jaxrs.sitechecker.domain.SiteResponse;
import java.util.Date;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author nick
 */
public class URLReader {

    public SiteResponse getUrl(Site site) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpUriRequest req = new HttpGet(site.getUrl());
            Date start = new Date();
            HttpResponse resp = client.execute(req);
            String respStr = EntityUtils.toString(resp.getEntity());
            Date end = new Date();
            SiteResponse respRet = new SiteResponse();
            respRet.setResponseTime(end.getTime() - start.getTime());
            respRet.setCode(resp.getStatusLine().getStatusCode());
            respRet.setResponse(respStr);
            respRet.setSite(site);
            return respRet;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
