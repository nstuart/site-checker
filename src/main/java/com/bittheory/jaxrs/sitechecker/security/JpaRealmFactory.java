/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker.security;

import java.util.Arrays;
import java.util.Collection;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import org.apache.shiro.ShiroException;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.RealmFactory;
import org.apache.shiro.util.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nick
 */
@Singleton
public class JpaRealmFactory implements RealmFactory, Initializable {

    private Logger log = LoggerFactory.getLogger(JpaRealmFactory.class);
    private static JpaRealm realm;

    @Override
    public Collection<Realm> getRealms() {
        return Arrays.asList((Realm) realm);
    }

    @Override
    public void init() throws ShiroException {
        if (realm == null) {
            log.debug("Initializing JPA Realm Factory");
            try {
                InitialContext initialContext = new InitialContext();
                BeanManager bm = (BeanManager) initialContext.lookup("java:comp/BeanManager");
                Bean<JpaRealm> realmBean = (Bean<JpaRealm>) bm.getBeans(JpaRealm.class).iterator().next();
                CreationalContext<JpaRealm> realmBeanContext = bm.createCreationalContext(realmBean);
                realm = (JpaRealm) bm.getReference(realmBean, JpaRealm.class, realmBeanContext);
                log.debug("Successfully found JpaRealm bean instance.");
            } catch (Exception ex) {
                throw new ShiroException("Error initializing JpaRealm.", ex);
            }

        }
    }
}
