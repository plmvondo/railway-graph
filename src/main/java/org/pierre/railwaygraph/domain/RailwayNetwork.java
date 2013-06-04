/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Pierre
 */
@Entity
@Table(name = "RAILWAY_NETWORK")
public class RailwayNetwork implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Set<Section> sections = new HashSet<Section>();
    private Set<RailwayStation> railwayStations = new HashSet<RailwayStation>();
    private Set<Path> paths = new HashSet<Path>();
    private Date created;
    private String name;
    private int version = 0;
    static final Comparator<List<Section>> ARC_LIST_COMPARATOR = new Comparator<List<Section>>() {
        public int compare(List<Section> a, List<Section> b) {
            RailwayNetwork r = new RailwayNetwork();
            int cmp = r.computeDistance(a).compareTo(r.computeDistance(b));
            if (cmp != 0) 
                return cmp;
            
            return r.concatNames(a).compareTo(r.concatNames(b));
        }
    };
    
    
    public RailwayNetwork() {
        this.created = new Date();
    }

    public RailwayNetwork(String name) {
        this.created = new Date();
        this.name = name;
    }

    public RailwayNetwork(Set<Section> sections, Set<RailwayStation> railwayStations, Set<Path> paths, String name) {
        this.sections = sections;
        this.railwayStations = railwayStations;
        this.paths = paths;
        this.created = new Date();
        this.name = name;
    }
    
    
    
    

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RAILWAY_NETWORK_ID")
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
        @Column(name = "NAME", length = 255, nullable = false, unique = true)
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
    @OneToMany(mappedBy = "railwayNetwork", cascade = CascadeType.ALL)
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    public Set<Section> getSections() {
        return sections;
    }

    /**
     * Set the value of departureSections
     *
     * @param departureSections new value of departureSections
     */
    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }
    
    
    public void addSection(Section section) {
        if (section == null) throw new IllegalArgumentException("section is NULL!");
        section.setRailwayNetwork(this);
        getSections().add(section);
    }
    
    
    /**
     * Get the value of railwayStations
     *
     * @return the value of railwayStations
     */
    @OneToMany(mappedBy = "railwayNetwork", cascade = CascadeType.ALL)
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    public Set<RailwayStation> getRailwayStations() {
        return railwayStations;
    }

    /**
     * Set the value of railwayStations
     *
     * @param railwayStations new value of railwayStations
     */
    public void setRailwayStations(Set<RailwayStation> railwayStations) {
        this.railwayStations = railwayStations;
    }
    
    
    public void addRailwayStation(RailwayStation railwayStation) {
        if (railwayStation == null) throw new IllegalArgumentException("railway station is NULL!");
        railwayStation.setRailwayNetwork(this);
        getRailwayStations().add(railwayStation);
    }
    

    /**
     * Get the value of paths
     *
     * @return the value of paths
     */
    @OneToMany(mappedBy = "railwayNetwork", cascade = CascadeType.ALL)
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    public Set<Path> getPaths() {
        return paths;
    }

    /**
     * Set the value of paths
     *
     * @param paths new value of paths
     */
    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }
    
    
    
    public void addPath(Path path) {
        if (path == null) throw new IllegalArgumentException("path is NULL!");
        path.setRailwayNetwork(this);
        getPaths().add(path);
    }

    
    
    public List<Section> shortestPath(RailwayStation departureStation, RailwayStation arrivalStation) {
        List<Section> path = new ArrayList<Section>();
        Queue<List<Section>> queue = new PriorityQueue<List<Section>>(15, ARC_LIST_COMPARATOR);
        Map<String, Float> cloud = new HashMap<String, Float>();
        while(!departureStation.equals(arrivalStation)) {
            if (!cloud.containsKey(departureStation.getName())) {
                cloud.put(departureStation.getName(), computeDistance(path));
                
                Set<Section> departureSections = new HashSet<Section>(departureStation.getDepartureSections());
                
                for (Section section : departureSections) {
                    if (!cloud.containsKey(section.getArrivalStation().getName())) {
                        List<Section> newPath = new ArrayList<Section>(path);
                        newPath.add(section);
                        queue.add(newPath);
                    }
                }
            }
            if (queue.isEmpty()) return new ArrayList<Section>();
            path = queue.remove();
            int index = path.size() - 1;
            departureStation = path.get(index).getArrivalStation();
        }
        return path;
    }
    
    private Float computeDistance(List<Section> path) {
        Float distance = new Float(0.0);
        for(Section section: path) {
            distance += section.getNbKms();
        }
        return distance;
    }
    
    private String concatNames(List<Section> path) {
        String concatenatedNames = "";
        for(Section section: path) {
            concatenatedNames += section.getName();
        }
        return concatenatedNames;
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
        
        if (!(otherObj instanceof RailwayNetwork)) {
            return false;
        }
        
        final RailwayNetwork other = (RailwayNetwork) otherObj;
        
        return (((getName() == null) ? (other.getName() == null)
                : getName().equals(other.getName()))
                && (getCreated().getTime() == other.getCreated().getTime()));
    }

    @Override
    public String toString() {
        return "Railway network ('" + getId() + "'), Name: '" + getName() + "'";
    }
    
    
}
