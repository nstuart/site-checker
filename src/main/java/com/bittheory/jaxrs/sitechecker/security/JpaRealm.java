/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker.security;

import com.bittheory.jaxrs.sitechecker.UserManager;
import com.bittheory.jaxrs.sitechecker.domain.User;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nick
 */
@Singleton
public class JpaRealm implements Realm {

    private Logger log = LoggerFactory.getLogger(JpaRealm.class);
    @Inject
    private CredentialsMatcher credMatcher;
    @Inject
    private UserManager manager;

    @Override
    public String getName() {
        return "JPA Realm";
    }

    @Override
    public boolean supports(AuthenticationToken at) {
        return (at instanceof UsernamePasswordToken);
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) at;
        User user = manager.findByEmail(token.getUsername());
        SimpleAuthenticationInfo inf = new SimpleAuthenticationInfo(user, user.getPassword(), new SimpleByteSource(user.getSalt()), getName());
        boolean matches = credMatcher.doCredentialsMatch(at, inf);
        if (matches) {
            return inf;
        } else {
            log.warn("Invalid login attempt {}", token.getUsername());
            return null;
        }
    }
}
