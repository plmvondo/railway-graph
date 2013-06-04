/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
/**
 *
 * @author Pierre
 */
@Embeddable
public class PathSectionKey implements Serializable {
        
        private Long sectionId;
        private Long pathId;

    public PathSectionKey() {
    }

    public PathSectionKey(Long sectionId, Long pathId) {
        this.sectionId = sectionId;
        this.pathId = pathId;
    }       
    

    /**
     * Get the value of sectionId
     *
     * @return the value of sectionId
     */
    @Column(name = "_SECTION_ID")
    public Long getSectionId() {
        return sectionId;
    }

    /**
     * Set the value of sectionId
     *
     * @param sectionId new value of sectionId
     */
    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

   

    /**
     * Get the value of pathId
     *
     * @return the value of pathId
     */
    @Column(name = "_PATH_ID")
    public Long getPathId() {
        return pathId;
    }

    /**
     * Set the value of pathId
     *
     * @param pathId new value of pathId
     */
    public void setPathId(Long pathId) {
        this.pathId = pathId;
    }

    @Override
    public int hashCode() {
        return (((getPathId() != null) ? getPathId().hashCode() : 0)
                ^ (37 * getSectionId().hashCode()));
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }
        
        if (!(otherObj instanceof PathSectionKey)) {
            return false;
        }
        
        PathSectionKey other = (PathSectionKey) otherObj;
        
        return (((getPathId() == null) ? (other.getPathId() == null)
                : getPathId() == other.getPathId())
                && (getSectionId() == other.getSectionId()));
    }
    
    
    
    @Override
    public String toString() {
        return "Section: " + getSectionId() + " - Path: " + getPathId();
    }

    
}
