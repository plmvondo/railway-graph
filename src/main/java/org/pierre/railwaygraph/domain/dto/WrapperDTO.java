/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain.dto;

import java.util.List;

/**
 *
 * @author Pierre
 */
public class WrapperDTO {
    
            private RailwayNetworkDTO railwayNetworkDTO;
            private List<RailwayStationDTO> railwayStationsDTO;
            private List<SectionDTO> sectionsDTO;
            private List<PathDTO> pathsDTO;

            
    public WrapperDTO(RailwayNetworkDTO railwayNetworkDTO, List<RailwayStationDTO> railwayStationsDTO, List<SectionDTO> sectionsDTO, List<PathDTO> pathsDTO) {
        this.railwayNetworkDTO = railwayNetworkDTO;
        this.railwayStationsDTO = railwayStationsDTO;
        this.sectionsDTO = sectionsDTO;
        this.pathsDTO = pathsDTO;
    }
            
            

    /**
     * Get the value of railwayNetworkDTO
     *
     * @return the value of railwayNetworkDTO
     */
    public RailwayNetworkDTO getRailwayNetworkDTO() {
        return railwayNetworkDTO;
    }

    /**
     * Set the value of railwayNetworkDTO
     *
     * @param railwayNetworkDTO new value of railwayNetworkDTO
     */
    public void setRailwayNetworkDTO(RailwayNetworkDTO railwayNetworkDTO) {
        this.railwayNetworkDTO = railwayNetworkDTO;
    }

        

    /**
     * Get the value of railwayStationsDTO
     *
     * @return the value of railwayStationsDTO
     */
    public List<RailwayStationDTO> getRailwayStationsDTO() {
        return railwayStationsDTO;
    }

    /**
     * Set the value of railwayStationsDTO
     *
     * @param railwayStationsDTO new value of railwayStationsDTO
     */
    public void setRailwayStationsDTO(List<RailwayStationDTO> railwayStationsDTO) {
        this.railwayStationsDTO = railwayStationsDTO;
    }

        

    /**
     * Get the value of sectionsDTO
     *
     * @return the value of sectionsDTO
     */
    public List<SectionDTO> getSectionsDTO() {
        return sectionsDTO;
    }

    /**
     * Set the value of sectionsDTO
     *
     * @param sectionsDTO new value of sectionsDTO
     */
    public void setSectionsDTO(List<SectionDTO> sectionsDTO) {
        this.sectionsDTO = sectionsDTO;
    }
    
        

    /**
     * Get the value of pathsDTO
     *
     * @return the value of pathsDTO
     */
    public List<PathDTO> getPathsDTO() {
        return pathsDTO;
    }

    /**
     * Set the value of pathsDTO
     *
     * @param pathsDTO new value of pathsDTO
     */
    public void setPathsDTO(List<PathDTO> pathsDTO) {
        this.pathsDTO = pathsDTO;
    }

}
