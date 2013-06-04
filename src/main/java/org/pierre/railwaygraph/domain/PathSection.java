/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Pierre
 */
@Embeddable
public class PathSection implements Serializable {
    private static final long serialVersionUID = 1L;
  
        private Section section;
        private Path path;
        private boolean departureStationServed;
        private boolean arrivalStationServed;
        private Date created;
        

    public PathSection() {
        this.created = new Date();
    }

    public PathSection(Section section, Path path, boolean departureStationServed, boolean arrivalStationServed) {
        this.section = section;
        this.path = path;
        this.departureStationServed = departureStationServed;
        this.arrivalStationServed = arrivalStationServed;
        this.created = new Date();
    }

    
    public PathSection(Section section, Path path) {
        this.section = section;
        this.path = path;
        this.created = new Date();
    }
    

    
    /**
     * Get the value of section
     *
     * @return the value of section
     */
    @ManyToOne
    @JoinColumn(name="_SECTION_ID", nullable = false, updatable = false)
    public Section getSection() {
        return section;
    }

    /**
     * Set the value of section
     *
     * @param section new value of section
     */
    public void setSection(Section section) {
        this.section = section;
    }
    

    /**
     * Get the value of path
     *
     * @return the value of path
     */
    @org.hibernate.annotations.Parent
    public Path getPath() {
        return path;
    }

    /**
     * Set the value of path
     *
     * @param path new value of path
     */
    public void setPath(Path path) {
        this.path = path;
    }


    
    

    /**
     * Get the value of departureStationServed
     *
     * @return the value of departureStationServed
     */
    @Column(name = "DEP_STATION_SERVED", nullable = false)
    public boolean isDepartureStationServed() {
        return departureStationServed;
    }

    /**
     * Set the value of departureStationServed
     *
     * @param departureStationServed new value of departureStationServed
     */
    public void setDepartureStationServed(boolean departureStationServed) {
        this.departureStationServed = departureStationServed;
    }


    /**
     * Get the value of arrivalStationServed
     *
     * @return the value of arrivalStationServed
     */
    @Column(name = "ARR_STATION_SERVED", nullable = false)
    public boolean isArrivalStationServed() {
        return arrivalStationServed;
    }

    /**
     * Set the value of arrivalStationServed
     *
     * @param arrivalStationServed new value of arrivalStationServed
     */
    public void setArrivalStationServed(boolean arrivalStationServed) {
        this.arrivalStationServed = arrivalStationServed;
    }
    
        

    /**
     * Get the value of created
     *
     * @return the value of created
     */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name="CREATED", nullable = false, updatable = false)
    public Date getCreated() {
        return created;
    }

    /**
     * Set the value of created
     *
     * @param created new value of created
     */
    public void setCreated(Date created) {
        this.created = created;
    }




    @Override
    public int hashCode() {
        return getSection().getId().hashCode() ^ (37 * getPath().getId().hashCode()) ^ (37 * getCreated().hashCode());
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }
        
        if (!(otherObj instanceof PathSection)) {
            return false;
        }
        
        final PathSection other = (PathSection) otherObj;
        
        return (((getPath().getId() == null) ? (other.getPath().getId() == null)
                : getPath().getId().equals(other.getPath().getId()))
                && (getSection().getId() == other.getSection().getId())
                && (getCreated().getTime() == other.getCreated().getTime()));
    }

    @Override
    public String toString() {
        return "Path ('" + getPath().getId() + "'), Section: ('" + getSection().getId() + "')";
    }
    
}
