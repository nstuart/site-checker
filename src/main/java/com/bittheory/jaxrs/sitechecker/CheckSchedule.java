/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

import com.bittheory.cdi.scheduled.Scheduled;
import com.bittheory.jaxrs.sitechecker.domain.Site;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nstuart1
 */
@Scheduled("0 * * ? * *")
public class CheckSchedule implements Runnable{

    private Logger log = LoggerFactory.getLogger(CheckSchedule.class);
    
    @Inject
    private SiteManager siteManager;
    
    @Override
    public void run() {
        log.info("RUNNING!");
        List<Site> sites = siteManager.getAll();
        
        for (Site site : sites) {
            log.info("Pinging site: " + site.getUrl());
            siteManager.ping(site);
        }
    }
    
    
}
