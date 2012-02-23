/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

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

    public SiteResponse getUrl(String url) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpUriRequest req = new HttpGet(url);
            Date start = new Date();
            HttpResponse resp = client.execute(req);
            String respStr = EntityUtils.toString(resp.getEntity());
            Date end = new Date();
            SiteResponse respRet = new SiteResponse();
            respRet.setResponseTime(end.getTime() - start.getTime());
            respRet.setCode(resp.getStatusLine().getStatusCode());
            respRet.setResponse(respStr);
            respRet.setSite(url);
            return respRet;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
