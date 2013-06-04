/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain.dto;

import org.pierre.railwaygraph.domain.PathSection;

/**
 *
 * @author Pierre
 */
public class PathSectionDTO {
    
        private String id;
        private String departureStationName;
        private String arrivalStationName;
        private String departureStationServed;
        private String arrivalStationServed;

        
        
    public PathSectionDTO(PathSection pathSection) {
        this.id = pathSection.getSection().getId() + "_" + pathSection.getPath().getId();
        this.departureStationName = pathSection.getSection().getDepartureStation().getName();
        this.arrivalStationName = pathSection.getSection().getArrivalStation().getName();
        this.departureStationServed = "" + pathSection.isDepartureStationServed();
        this.arrivalStationServed = "" + pathSection.isArrivalStationServed();
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
     * Get the value of departureStationName
     *
     * @return the value of departureStationName
     */
    public String getDepartureStationName() {
        return departureStationName;
    }
    
    /**
     * Get the value of arrivalStationName
     *
     * @return the value of arrivalStationName
     */
    public String getArrivalStationName() {
        return arrivalStationName;
    }
    
    /**
     * Get the value of departureStationServed
     *
     * @return the value of departureStationServed
     */
    public String getDepartureStationServed() {
        return departureStationServed;
    }
    
    /**
     * Get the value of arrivalStationServed
     *
     * @return the value of arrivalStationServed
     */
    public String getArrivalStationServed() {
        return arrivalStationServed;
    }
   
}
