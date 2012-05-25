/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker.security;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;

/**
 *
 * @author nick
 */
@Singleton
public class PasswordUtils {
    public static final int HASH_ITERS = 2000;
    private HashedCredentialsMatcher credentialsMatcher;
    
    private RandomNumberGenerator rand = new SecureRandomNumberGenerator();
    
    @PostConstruct
    public void init(){
        credentialsMatcher = new HashedCredentialsMatcher(Sha512Hash.ALGORITHM_NAME);
        credentialsMatcher.setHashIterations(HASH_ITERS);
    }
    
    public String generateSalt(){
        return rand.nextBytes().toBase64();
    }
    
    public String hash(String password, String salt){
        Sha512Hash sha512Hash = new Sha512Hash(password, salt, HASH_ITERS);
        return sha512Hash.toString();
    }

    @Produces @Singleton
    public CredentialsMatcher getCredentialsMatcher() {
        return credentialsMatcher;
    }
    
}
