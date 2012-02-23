/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker.jenkins;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONUnmarshaller;
import java.io.StringReader;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBException;

/**
 *
 * @author nstuart1
 */
@ApplicationScoped
public class BuildContext {

    private Class[] types = new Class[]{Build.class, Culprits.class};
    private JSONJAXBContext newInstance;

    @PostConstruct
    public void init() throws JAXBException {
        newInstance = new JSONJAXBContext(JSONConfiguration.mapped().build(), types);
    }

    public Build unmarshal(String json) {
        try {
            JSONUnmarshaller unmarsh = newInstance.createJSONUnmarshaller();
            return unmarsh.unmarshalFromJSON(new StringReader(json), Build.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
