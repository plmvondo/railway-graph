/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.pierre.railwaygraph.exceptions.BusinessRuleException;

/**
 *
 * @author Pierre
 */
@Entity
@Table(name = "_PATH")
public class Path implements Serializable, Comparable<Path> {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private Float nbKms = new Float(0.0);
    private Date created;
    private RailwayStation departureStation;
    private RailwayStation arrivalStation;
    private RailwayNetwork railwayNetwork;
    private List<RailwayStation> servedStations = new ArrayList<RailwayStation>();
    private List<PathSection> pathSection = new ArrayList<PathSection>();
    private int version = 0;
    

    public Path() {
        this.created = new Date();
    }

    
    @TableGenerator(
            name = "pathGenerator",
            table = "PERSISTENCE_SEQUENCE_GENERATOR",
            pkColumnName = "GENERATOR_KEY",
            valueColumnName = "GENERATOR_VALUE",
            pkColumnValue = "_PATH_ID",
            allocationSize = 1) 
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pathGenerator")
    @Column(name = "_PATH_ID")
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
        @Column(name="_PATH_NAME", length = 255, nullable = false, unique = true)
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
    @Column(name="NB_KMS")
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
        
        

    /**
     * Get the value of departureStation
     *
     * @return the value of departureStation
     */
    @ManyToOne
    @JoinColumn(name="FK_DEPARTURE_STATION_ID", referencedColumnName="RAILWAY_STATION_ID", nullable = false)
    public RailwayStation getDepartureStation() {
        return departureStation;
    }

    /**
     * Set the value of departureStation
     *
     * @param departureStation new value of departureStation
     */
    public void setDepartureStation(RailwayStation departureStation) {
        this.departureStation = departureStation;
    }

    

    /**
     * Get the value of arrivalStation
     *
     * @return the value of arrivalStation
     */
    @ManyToOne
    @JoinColumn(name="FK_ARRIVAL_STATION_ID", referencedColumnName="RAILWAY_STATION_ID", nullable = false)
    public RailwayStation getArrivalStation() {
        return arrivalStation;
    }

    /**
     * Set the value of arrivalStation
     *
     * @param arrivalStation new value of arrivalStation
     */
    public void setArrivalStation(RailwayStation arrivalStation) {
        this.arrivalStation = arrivalStation;
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
   

    
    

    /**
     * Get the value of servedStations
     *
     * @return the value of servedStations
     */
    @ManyToMany
    @JoinTable(name = "PATH_STATION",
            joinColumns = 
                @JoinColumn(name = "PATHS_ID", referencedColumnName = "_PATH_ID"),
            inverseJoinColumns = 
                @JoinColumn(name = "STATIONS_ID", referencedColumnName = "RAILWAY_STATION_ID")
            )
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    @org.hibernate.annotations.IndexColumn(name = "SERVED_STATION_POSITION")
    public List<RailwayStation> getServedStations() {
        return servedStations;
    }

    /**
     * Set the value of servedStations
     *
     * @param servedStations new value of servedStations
     */
    public void setServedStations(List<RailwayStation> servedStations) {
        this.servedStations = servedStations;
    }
        

    /**
     * Get the value of pathSection
     *
     * @return the value of pathSection
     */
        @ElementCollection(fetch=FetchType.EAGER)
        @JoinTable(
            name = "PATH_SECTION",
            joinColumns = @JoinColumn(name = "_PATH_ID")
        )
    public List<PathSection> getPathSection() {
        return pathSection;
    }

    /**
     * Set the value of pathSection
     *
     * @param pathSection new value of pathSection
     */
    public void setPathSection(List<PathSection> pathSection) {
        this.pathSection = pathSection;
    }
    
    
    public void addPathSection(PathSection pathSection) {
        if (pathSection == null) throw new IllegalArgumentException("path section is NULL !");
        
        if (this.checkBusinessRules(pathSection.getSection(), pathSection.isDepartureStationServed())) {
            this.getPathSection().add(pathSection);
            this.setNbKms(this.getNbKms() + pathSection.getSection().getNbKms());
            if (pathSection.isDepartureStationServed() && !alreadyFound(this.getServedStations(), pathSection.getSection().getDepartureStation()) ) {
                this.getServedStations().add(pathSection.getSection().getDepartureStation());
            }
            if (pathSection.isArrivalStationServed()) {
                this.getServedStations().add(pathSection.getSection().getArrivalStation());
            }
        }
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
        
        if (!(otherObj instanceof Path)) {
            return false;
        }
        
        final Path other = (Path) otherObj;
        
        return (((getName() == null) ? (other.getName() == null)
                : getName().equals(other.getName()))
                && (getCreated().getTime() == other.getCreated().getTime()));
    }

    @Override
    public String toString() {
        return "Path ('" + getId() + "'), Name: '" + getName() + "', Departure station: '" + 
                getDepartureStation() + "', Arrival station: '" + getArrivalStation() + "', Number of Kms: "
                + getNbKms() + "'";
    }

    @Override
    public int compareTo(Path t) {
        return getNbKms().compareTo(t.getNbKms());
    }

    @Override
    protected Path clone() throws CloneNotSupportedException {
        return (Path) super.clone();
    }
    
    // ********************** Business Methods ********************** //
    
    private boolean checkBusinessRules(Section section, boolean departureStationServed) throws BusinessRuleException {
        boolean flag = true;
        
        if (this.getPathSection().isEmpty()) {
            // Check that the path departure station and that of the first added section are the same
            if (!(this.getDepartureStation().getName() == null ? section.getDepartureStation().getName() == null : this.getDepartureStation().getName().equals(section.getDepartureStation().getName()))) {
                throw new BusinessRuleException("The path departure station and that of the first section are not the same !");
            }
            // Check that the departure station is served, if the added section is the path's first
            if (!departureStationServed)
                throw new BusinessRuleException("The section is the first one of the path: the departure station must be served !");
        }
        else {
            // Check that the departure station of the added section is not the arrival station of the path
            if (this.getArrivalStation().getName() == null ? section.getDepartureStation().getName() == null : this.getArrivalStation().getName().equals(section.getDepartureStation().getName())) {
                throw new BusinessRuleException("The path is defined: you can't add another section !");
            }
            // Check that the departure station of the added section starts from the last station of the
            // path defined so far
            int lastIndex = this.getPathSection().size() - 1;
            PathSection lastPathSection = this.getPathSection().get(lastIndex);
            if (!(lastPathSection.getSection().getArrivalStation().getName() == null ? section.getDepartureStation().getName() == null : lastPathSection.getSection().getArrivalStation().getName().equals(section.getDepartureStation().getName()))) {
                throw new BusinessRuleException("The sections are not in the right order !");
            }
        }
        return flag;
    }
    
    public boolean finalCheck() {
        boolean flag = true;
        int lastIndex = this.getPathSection().size() - 1;
        PathSection lastPathSection = this.getPathSection().get(lastIndex);
        if (!(lastPathSection.getSection().getArrivalStation().getName() == null ? this.getArrivalStation().getName() == null : lastPathSection.getSection().getArrivalStation().getName().equals(this.getArrivalStation().getName()))) {
            throw new BusinessRuleException("The arrival station of the path and that of the last section are not the same !");
        }
        if (!(lastPathSection.isArrivalStationServed())) {
            throw new BusinessRuleException("The arrival station of the last added section must be served !");
        }
        return flag;
    }
    
    private boolean alreadyFound(List<RailwayStation> servedStations, RailwayStation station) {
        // Required copy
        List<RailwayStation> stations = new ArrayList<RailwayStation>(servedStations);
        Collections.sort(stations);
        return (Collections.binarySearch(stations, station) >= 0);
    }
    
}
