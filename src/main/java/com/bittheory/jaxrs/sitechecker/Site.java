/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nick
 */
@XmlRootElement
public class Site {
    
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
