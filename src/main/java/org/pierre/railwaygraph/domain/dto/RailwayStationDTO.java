/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain.dto;

import java.util.Date;
import org.pierre.railwaygraph.domain.RailwayStation;

/**
 *
 * @author Pierre
 */
public class RailwayStationDTO {
    
        private String id;
        private String parent;
        private String name;
        private Date created;



    public RailwayStationDTO(RailwayStation station) {
        this.id = "station_" + station.getId();
        this.parent = "_Stations";
        this.name = station.getName();
        this.created = station.getCreated();
    }

    public RailwayStationDTO(String id, String name, String parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
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
     * Get the value of parent
     *
     * @return the value of parent
     */
    public String getParent() {
        return parent;
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

}
