/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.sitechecker.jenkins;

import com.bittheory.jaxrs.sitechecker.jenkins.Build;
import com.bittheory.jaxrs.sitechecker.jenkins.Culprits;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONUnmarshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nstuart1
 */
public class JAXBBindTest {
   
    @Test
    public void unmarshal() throws JAXBException{
        Class[] types = new Class[]{Build.class, Culprits.class};
        JSONJAXBContext newInstance = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
        JSONUnmarshaller unmarsh = newInstance.createJSONUnmarshaller();
        Build bld = unmarsh.unmarshalFromJSON(getClass().getClassLoader().getResourceAsStream("sample-jenkins.json"), Build.class);
        assertFalse(bld.isBuilding());
        assertEquals("SUCCESS", bld.getResult());
        assertNotNull(bld.getCulprits());
        assertEquals("tester", bld.getCulprits().getFullName());
        System.out.println(bld.getCulprits().getFullName());
    }
    
}
