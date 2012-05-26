/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.jaxrs.sitechecker.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author nick
 */
@XmlRootElement
@Entity
public class Site extends Base{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    private String name;
    
    @OneToMany(mappedBy="site", cascade= CascadeType.ALL)
    private List<SiteResponse> responses = new ArrayList<SiteResponse>();
    
    @NotNull
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public List<SiteResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<SiteResponse> responses) {
        this.responses = responses;
    }
    
    
}
