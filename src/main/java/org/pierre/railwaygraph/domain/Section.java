/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Pierre
 */
@Entity
@Table(name = "_SECTION")
public class Section implements Serializable, Comparable<Section> {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private Float nbKms;
    private Date created;
    private RailwayStation arrivalStation;
    private RailwayStation departureStation;
    private RailwayNetwork railwayNetwork;
    private int version = 0;
    

    public Section() {
        this.created = new Date();
    }

    
    @TableGenerator(
            name = "sectionGen",
            table = "PERSISTENCE_SEQUENCE_GENERATOR",
            pkColumnName = "GENERATOR_KEY",
            valueColumnName = "GENERATOR_VALUE",
            pkColumnValue = "_SECTION_ID",
            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sectionGen")
    @Column(name = "_SECTION_ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    

    /**
     * Get the value of version
     *
     * @return the value of version
     */
        @Version
    @Column(name = "OBJ_VERSION")
    public int getVersion() {
        return version;
    }

    
    /**
     * Set the value of version
     *
     * @param version new value of version
     */
    public void setVersion(int version) {
        this.version = version;
    }
    

    /**
     * Get the value of name
     *
     * @return the value of name
     */
        @Column(name="_SECTION_NAME", length = 255, nullable = false, unique = true)
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

        

    /**
     * Get the value of nbKms
     *
     * @return the value of nbKms
     */
    @Column(name="NB_KMS", nullable = false)
    public Float getNbKms() {
        return nbKms;
    }

    /**
     * Set the value of nbKms
     *
     * @param nbKms new value of nbKms
     */
    public void setNbKms(Float nbKms) {
        this.nbKms = nbKms;
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


     
    @ManyToOne
    @JoinColumn(name="FK_ARRIVAL_STATION_ID", referencedColumnName="RAILWAY_STATION_ID", nullable = false)
    public RailwayStation getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(RailwayStation arrivalStation) {
        this.arrivalStation = arrivalStation;
    }
    

    /**
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name="FK_DEPARTURE_STATION_ID", referencedColumnName="RAILWAY_STATION_ID", nullable = false)
    public RailwayStation getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(RailwayStation departureStation) {
        this.departureStation = departureStation;
    }
    
    

    /**
     * Get the value of railwayNetwork
     *
     * @return the value of railwayNetwork
     */
    @ManyToOne
    @JoinColumn(name="FK_RAILWAY_NETWORK_ID", referencedColumnName="RAILWAY_NETWORK_ID", nullable = false)
    public RailwayNetwork getRailwayNetwork() {
        return railwayNetwork;
    }
    
    
    /**
     * Set the value of railwayNetwork
     *
     * @param railwayNetwork new value of railwayNetwork
     */
    public void setRailwayNetwork(RailwayNetwork railwayNetwork) {
        this.railwayNetwork = railwayNetwork;
    }



    @Override
    public int hashCode() {
        return (((getName() != null) ? getName().hashCode() : 0)
                ^ (37 * getCreated().hashCode()));
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }
        
        if (!(otherObj instanceof Section)) {
            return false;
        }
        
        final Section other = (Section) otherObj;
        
        return (((getName() == null) ? (other.getName() == null)
                : getName().equals(other.getName()))
                && (getCreated().getTime() == other.getCreated().getTime()));
    }

    @Override
    public String toString() {
        return "Section ('" + getId() + "'), Name: '" + getName() + "', Departure station: '" + 
                getDepartureStation() + "', Arrival station: '" + getArrivalStation() + "', Number of Kms: "
                + getNbKms() + "'";
    }

    public int compareTo(Section a) {
        return getNbKms().compareTo(a.getNbKms());
    }
    
    
}
