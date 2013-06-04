/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain.dto;

import java.util.Date;
import org.pierre.railwaygraph.domain.RailwayNetwork;

/**
 *
 * @author Pierre
 */
public class RailwayNetworkDTO {
    
        private String id;
        private String name;
        private Date created;
        private Long databaseId;


        
    public RailwayNetworkDTO(RailwayNetwork railwayNetwork) {
        this.id = "root";
        this.name = railwayNetwork.getName();
        this.created = railwayNetwork.getCreated();
        this.databaseId = railwayNetwork.getId();
    }

 

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the value of created
     *
     * @return the value of created
     */
    public Date getCreated() {
        return created;
    }
    
    /**
     * Get the value of databaseId
     *
     * @return the value of databaseId
     */
    public Long getDatabaseId() {
        return databaseId;
    }
  
}
