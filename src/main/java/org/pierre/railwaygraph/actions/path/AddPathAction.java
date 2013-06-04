/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.path;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.SectionDAO;
import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.dao.RailwayNetworkDAO;
import org.pierre.railwaygraph.dao.PathDAO;
import org.pierre.railwaygraph.domain.Section;
import org.pierre.railwaygraph.domain.PathSection;
import org.pierre.railwaygraph.domain.RailwayStation;
import org.pierre.railwaygraph.domain.RailwayNetwork;
import org.pierre.railwaygraph.domain.Path;
import org.pierre.railwaygraph.domain.dto.PathDTO;
import org.pierre.railwaygraph.exceptions.BusinessRuleException;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class AddPathAction extends BaseAction {
    
        private String pathName;
        private String departureStationId;
        private String arrivalStationId;
        private List<Map> sectionsArray;
        private String railwayNetworkName;
        private PathDAO pathDAO;
        private SectionDAO sectionDAO;
        private RailwayStationDAO railwayStationDAO;
        private RailwayNetworkDAO railwayNetworkDAO;
        private PathDTO pathDTO;
        private String errorMessage = "";
        


    /**
     * Set the value of pathName
     *
     * @param pathName new value of pathName
     */
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
    
    /**
     * Set the value of departureStationId
     *
     * @param departureStationId new value of departureStationId
     */
    public void setDepartureStationId(String departureStationId) {
        this.departureStationId = departureStationId;
    }
    
    /**
     * Set the value of arrivalStationId
     *
     * @param arrivalStationId new value of arrivalStationId
     */
    public void setArrivalStationId(String arrivalStationId) {
        this.arrivalStationId = arrivalStationId;
    }
    
    /**
     * Set the value of sectionsArray
     *
     * @param sectionsArray new value of sectionsArray
     */
    public void setSectionsArray(List<Map> sectionsArray) {
        this.sectionsArray = sectionsArray;
    }
    
    /**
     * Set the value of railwayNetworkName
     *
     * @param railwayNetworkName new value of railwayNetworkName
     */
    public void setRailwayNetworkName(String railwayNetworkName) {
        this.railwayNetworkName = railwayNetworkName;
    }
    
    /**
     * Set the value of pathDAO
     *
     * @param pathDAO new value of pathDAO
     */
    public void setPathDAO(PathDAO pathDAO) {
        this.pathDAO = pathDAO;
    }
    
    /**
     * Set the value of sectionDAO
     *
     * @param sectionDAO new value of sectionDAO
     */
    public void setSectionDAO(SectionDAO sectionDAO) {
        this.sectionDAO = sectionDAO;
    }
    
    /**
     * Set the value of railwayStationDAO
     *
     * @param railwayStationDAO new value of railwayStationDAO
     */
    public void setRailwayStationDAO(RailwayStationDAO railwayStationDAO) {
        this.railwayStationDAO = railwayStationDAO;
    }
    
    /**
     * Set the value of railwayNetworkDAO
     *
     * @param railwayNetworkDAO new value of railwayNetworkDAO
     */
    public void setRailwayNetworkDAO(RailwayNetworkDAO railwayNetworkDAO) {
        this.railwayNetworkDAO = railwayNetworkDAO;
    }
    
    /**
     * Get the value of pathDTO
     *
     * @return the value of pathDTO
     */
    public PathDTO getPathDTO() {
        return pathDTO;
    }
    
    /**
     * Get the value of errorMessage
     *
     * @return the value of errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    

    @Override
    public String execute() throws Exception {
        String[] tab = departureStationId.split("_");
        departureStationId = tab[1];
        String[] tab2 = arrivalStationId.split("_");
        arrivalStationId = tab2[1];
        try {
            RailwayStation departureStation = railwayStationDAO.fetchByIdWithCollections(Long.valueOf(departureStationId), "departurePaths");
            RailwayStation arrivalStation = railwayStationDAO.fetchByIdWithCollections(Long.valueOf(arrivalStationId), "arrivalPaths");
            Path path = new Path();
            path.setName(pathName);
            departureStation.addDeparturePath(path);
            arrivalStation.addArrivalPath(path);
            for(Map map: sectionsArray) {
                Map newMap = new HashMap(map);
                String[] array = newMap.get("pathSectionId").toString().split("_");
                String pathSectionId = array[1];
                Section section = sectionDAO.findById(Long.valueOf(pathSectionId));
                boolean flag1, flag2;
                if ("true".equals(newMap.get("departureStationServed").toString())) {
                    flag1 = true;
                }
                else {
                    flag1 = false;
                }
                if ("true".equals(newMap.get("arrivalStationServed").toString())) {
                    flag2 = true;
                }
                else {
                    flag2 = false;
                }
                PathSection pathSection = new PathSection(section, path, flag1, flag2);
                path.addPathSection(pathSection);
            }
            if (path.finalCheck()) {
                List<RailwayNetwork> railwayNetwork = railwayNetworkDAO.fetchByNameWithCollections(railwayNetworkName, "paths");
                railwayNetwork.get(0).addPath(path);
                railwayNetworkDAO.makePersistent(railwayNetwork.get(0));
                pathDTO = new PathDTO(pathDAO.findByName(pathName).get(0));
            }
            return SUCCESS;
        } catch(EmptyResultDataAccessException erdae) {
            errorMessage = getText("addPath.EmptyResultDataAccessException");
            return ERROR;
        } catch (DataIntegrityViolationException dive) {
            errorMessage = getText("addPath.dataIntegrityViolationException");
            return ERROR;
        } catch(DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        } catch (BusinessRuleException bre) {
            errorMessage = bre.getMessage();
            return ERROR;
        }
    }   
}
