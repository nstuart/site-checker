/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

import com.bittheory.jaxrs.sitechecker.domain.Site;
import com.bittheory.jaxrs.sitechecker.domain.SiteResponse;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author nstuart1
 */
@Stateless
public class SiteManager {

    @PersistenceContext
    private EntityManager em;
    @Inject
    private URLReader reader;

    public List<Site> getAll() {
        return em.createQuery("SELECT s FROM Site s", Site.class).getResultList();
    }

    public Site load(long siteId) {
        return em.find(Site.class, siteId);
    }

    public Site create(Site newSite) {
        em.persist(newSite);
        ping(newSite);
        return newSite;
    }
    
    public List<SiteResponse> responsesFrom(Site site, Long start){
        Query qr = em.createQuery("SELECT r FROM SiteResponse r WHERE r.site = :site AND r.createdAt >= :start" , SiteResponse.class);
        qr.setParameter("site", site);
        qr.setParameter("start", new Date(start));
        return qr.getResultList();
    }
    
    /**
     * 
     * @param site
     * @param limit How many responses to limit
     * @return 
     */
    public List<SiteResponse> lastResponses(long siteId, int limit){
        Query qr = em.createQuery("SELECT r FROM SiteResponse r WHERE r.site.id = :site" , SiteResponse.class);
        qr.setMaxResults(limit);
        qr.setParameter("site", siteId);
        return qr.getResultList();
    }

    public SiteResponse ping(Site site) {
        site = em.find(Site.class, site.getId());
        SiteResponse resp = reader.getUrl(site);
        site.getResponses().add(resp);
        resp.setResponse(""); //dont save the response to the database...
        return resp;
    }

    public void delete(long id) {
        Site site = load(id);
        if(site != null){
            em.remove(site);
        }
    }
}
