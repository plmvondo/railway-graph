/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.pierre.railwaygraph.exceptions.BusinessRuleException;

/**
 *
 * @author Pierre
 */
@Entity
@Table(name = "RAILWAY_STATION")
public class RailwayStation implements Serializable, Comparable<RailwayStation> {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private Date created;
    private Set<Section> arrivalSections = new HashSet<Section>();
    private Set<Section> departureSections = new HashSet<Section>();
    private Set<Path> arrivalPaths = new HashSet<Path>();
    private Set<Path> departurePaths = new HashSet<Path>();
    private RailwayNetwork railwayNetwork;
    private int version = 0;
    private Set<Path> paths = new HashSet<Path>();
    
    
    public RailwayStation() {
        this.created = new Date();
    }

    public RailwayStation(String name) {
        this.created = new Date();
        this.name = name;
    }

    public RailwayStation(String name, Set<Section> arrivalSections, Set<Section> departureSections, RailwayNetwork railwayNetwork) {
        this.name = name;
        this.created = new Date();
        this.arrivalSections = arrivalSections;
        this.departureSections = departureSections;
        this.railwayNetwork = railwayNetwork;
    }
    
    
    
    

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RAILWAY_STATION_ID")
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
    @Column(name="RAILWAY_STATION_NAME", length = 255, nullable = false, unique = true)
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
     * Get the value of departureSections
     *
     * @return the value of departureSections
     */
    @OneToMany(mappedBy = "departureStation", fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    public Set<Section> getDepartureSections() {
        return departureSections;
    }

    /**
     * Set the value of departureSections
     *
     * @param departureSections new value of departureSections
     */
    public void setDepartureSections(Set<Section> departureSections) {
        this.departureSections = departureSections;
    }
    
    
    public void addDepartureSection(Section DepartureSection) throws BusinessRuleException {
        if (DepartureSection == null) throw new IllegalArgumentException("departure section is NULL!");
        if ((DepartureSection.getDepartureStation() != null)  && (!(DepartureSection.getDepartureStation().getName().equals(this.getName()))))
            throw new BusinessRuleException("This section already has a different departure station !");
        DepartureSection.setDepartureStation(this);
        getDepartureSections().add(DepartureSection);
    }



    /**
     * Get the value of arrivalSections
     *
     * @return the value of arrivalSections
     */
    @OneToMany(mappedBy = "arrivalStation", fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    public Set<Section> getArrivalSections() {
        return arrivalSections;
    }

    /**
     * Set the value of arrivalSections
     *
     * @param arrivalSections new value of arrivalSections
     */
    public void setArrivalSections(Set<Section> arrivalSections) {
        this.arrivalSections = arrivalSections;
    }
    
    
    public void addArrivalSection(Section arrivalSection) throws BusinessRuleException {
        if (arrivalSection == null) throw new IllegalArgumentException("arrival section is NULL!");
        if ((arrivalSection.getArrivalStation() != null)  && (!(arrivalSection.getArrivalStation().getName().equals(this.getName()))))
            throw new BusinessRuleException("This section already has a different arrival station !");
        arrivalSection.setArrivalStation(this);
        getArrivalSections().add(arrivalSection);
    }
    
    
    
    /**
     * Get the value of arrivalPaths
     *
     * @return the value of arrivalPaths
     */
    @OneToMany(mappedBy = "arrivalStation")
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    public Set<Path> getArrivalPaths() {
        return arrivalPaths;
    }

    /**
     * Set the value of arrivalPaths
     *
     * @param arrivalPaths new value of arrivalPaths
     */
    public void setArrivalPaths(Set<Path> arrivalPaths) {
        this.arrivalPaths = arrivalPaths;
    }
    
    
    public void addArrivalPath(Path arrivalPath) throws BusinessRuleException {
        if (arrivalPath == null) throw new IllegalArgumentException("arrival path is NULL!");
        if ((arrivalPath.getArrivalStation() != null)  && (!(arrivalPath.getArrivalStation().getName().equals(this.getName()))))
            throw new BusinessRuleException("This path already has a different arrival station !");
        arrivalPath.setArrivalStation(this);
        getArrivalPaths().add(arrivalPath);
    }
    
    
    
    /**
     * Get the value of departurePaths
     *
     * @return the value of departurePaths
     */
    @OneToMany(mappedBy = "departureStation")
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    public Set<Path> getDeparturePaths() {
        return departurePaths;
    }

    /**
     * Set the value of departurePaths
     *
     * @param departurePaths new value of departurePaths
     */
    public void setDeparturePaths(Set<Path> departurePaths) {
        this.departurePaths = departurePaths;
    }
    
    
    
    public void addDeparturePath(Path departurePath) throws BusinessRuleException {
        if (departurePath == null) throw new IllegalArgumentException("departure path is NULL!");
        if ((departurePath.getDepartureStation() != null)  && (!(departurePath.getDepartureStation().getName().equals(this.getName()))))
            throw new BusinessRuleException("This path already has a different departure station !");
        departurePath.setDepartureStation(this);
        getDeparturePaths().add(departurePath);
    }

    

    /**
     * Get the value of railwayNetwork
     *
     * @return the value of railwayNetwork
     */
    @ManyToOne
    @JoinColumn(name="FK_RAILWAY_NETWORK_ID", referencedColumnName="RAILWAY_NETWORK_ID")
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
    
    
    
    @ManyToMany(mappedBy = "servedStations")
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    public Set<Path> getPaths() {
        return paths;
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
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
        
        if (!(otherObj instanceof RailwayStation)) {
            return false;
        }
        
        final RailwayStation other = (RailwayStation) otherObj;
        
        return (((getName() == null) ? (other.getName() == null)
                : getName().equals(other.getName()))
                && (getCreated().getTime() == other.getCreated().getTime()));
    }

    @Override
    public String toString() {
        return "Railway station ('" + getId() + "'), Name: '" + getName() + "'";
    }

    public int compareTo(RailwayStation o) {
        return getName().compareTo(o.getName());
    }

    
}
