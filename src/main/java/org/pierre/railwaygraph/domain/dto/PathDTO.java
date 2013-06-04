/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.pierre.railwaygraph.domain.Section;
import org.pierre.railwaygraph.domain.PathSection;
import org.pierre.railwaygraph.domain.Path;

/**
 *
 * @author Pierre
 */
public class PathDTO {
    
        private String id;
        private String parent;
        private Float nbKms;
        private String name;
        private Date created;
        private List<PathSectionDTO> pathSectionsDTO = new ArrayList<PathSectionDTO>();
        private String sections = "";




    public PathDTO(Path path) {
        int i = 1;
        this.nbKms = path.getNbKms();
        this.id = "path_" + path.getId();
        this.parent = "_Paths";
        this.name = path.getName();
        this.created = path.getCreated();
        for(PathSection pathSection: path.getPathSection()) {
            Section a = pathSection.getSection();
            this.pathSectionsDTO.add(new PathSectionDTO(pathSection));
            this.sections += "{ " + i++ + ": " + a.getName() + " (" + pathSection.isDepartureStationServed() + ", " + pathSection.isArrivalStationServed()
                    + ") }, ";
        }
        this.sections = this.sections.substring(0, this.sections.length() - 2);
    }

    public PathDTO(String id, String name, String parent) {
        this.id = id;
        this.parent = parent;
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
     * Get the value of pathSectionsDTO
     *
     * @return the value of pathSectionsDTO
     */
    public List<PathSectionDTO> getPathSectionsDTO() {
        return pathSectionsDTO;
    }
    
    /**
     * Get the value of sections
     *
     * @return the value of sections
     */
    public String getSections() {
        return sections;
    }
    
}
