/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker;

import com.bittheory.jaxrs.sitechecker.domain.User;
import com.bittheory.jaxrs.sitechecker.security.PasswordUtils;
import java.math.BigInteger;
import java.security.SecureRandom;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.jndi.JndiObjectFactory;
import org.apache.shiro.util.Factory;

/**
 *
 * @author nick
 */
@Stateless
public class UserManager {

    @PersistenceContext
    private EntityManager em;
    @Inject
    private PasswordUtils pwUtils;

    /**
     *
     * @return true if at least one user exists.
     */
    public boolean hasUsers() {
        TypedQuery<Long> qr = em.createNamedQuery(User.QR_USER_COUNT, Long.class);
        return qr.getSingleResult() > 0;
    }

    public User createUser(User user) {
        User newUser = new User();
        user.setEmail(user.getEmail());
        String salt = pwUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(pwUtils.hash(user.getPassword(), salt));
        em.persist(user);
        return newUser;
    }

    public User findByEmail(String email) {
        TypedQuery<User> userQ = em.createNamedQuery(User.QR_USER_EMAIL, User.class);
        return userQ.setParameter("email", email).getSingleResult();
    }
}
