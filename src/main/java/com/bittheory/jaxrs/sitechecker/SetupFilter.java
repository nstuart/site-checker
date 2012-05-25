/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

import com.bittheory.jaxrs.sitechecker.domain.User;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author nick
 */
public class SetupFilter implements ServletContextListener{
    
    @Inject
    private UserManager userManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        //create default user if one does not exist....
        if(!userManager.hasUsers()){
            User def = new User();
            def.setEmail("admin@admin.com");
            def.setPassword("admin");
            userManager.createUser(def);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
    
}
