/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nick
 */
@XmlRootElement
@Entity
@NamedQueries({
    @NamedQuery(name=User.QR_USER_COUNT, query="SELECT COUNT(u) FROM User u"),
    @NamedQuery(name=User.QR_USER_EMAIL, query="SELECT u FROM User u WHERE u.email = :email")
})
@Table(name = "users")
public class User extends Base {

    public static final String QR_USER_COUNT = "User.count";
    public static final String QR_USER_EMAIL = "User.findByEmail";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String salt;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
