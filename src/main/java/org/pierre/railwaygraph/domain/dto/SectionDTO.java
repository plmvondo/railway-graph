/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain.dto;

import java.util.Date;
import org.pierre.railwaygraph.domain.Section;

/**
 *
 * @author Pierre
 */
public class SectionDTO {
    
        private String parent;
        private String id;
        private Float nbKms;
        private String name;
        private Date created;
        private String departureStation;
        private String arrivalStation;

        
    public SectionDTO(Section section) {
        this.nbKms = section.getNbKms();
        this.id = "section_" + section.getId();
        this.parent = "_Sections";
        this.name = section.getName();
        this.created = section.getCreated();
        this.departureStation = section.getDepartureStation().getName();
        this.arrivalStation = section.getArrivalStation().getName();
    }

    public SectionDTO(String id, String name, String parent) {
        this.parent = parent;
        this.id = id;
        this.name = name;
    }
        

        

    /**
     * Get the value of nbKms
     *
     * @return the value of nbKms
     */
    public Float getNbKms() {
        return nbKms;
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
    
    /**
     * Get the value of departureStation
     *
     * @return the value of departureStation
     */
    public String getDepartureStation() {
        return departureStation;
    }
    
    /**
     * Get the value of arrivalStation
     *
     * @return the value of arrivalStation
     */
    public String getArrivalStation() {
        return arrivalStation;
    }
    
    
}
