/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.railwaystation;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.List;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.dao.RailwayNetworkDAO;
import org.pierre.railwaygraph.domain.RailwayStation;
import org.pierre.railwaygraph.domain.RailwayNetwork;
import org.pierre.railwaygraph.domain.dto.RailwayStationDTO;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class AddRailwayStationAction extends BaseAction {
    
        private String railwayStationName;
        private RailwayStationDTO railwayStationDTO;
        private RailwayStationDAO railwayStationDAO;
        private RailwayNetworkDAO railwayNetworkDAO;
        private String railwayNetworkName;
        private String errorMessage = "";
        
    
    /**
     * Set the value of railwayStationName
     *
     * @param railwayStationName new value of railwayStationName
     */
    public void setRailwayStationName(String railwayStationName) {
        this.railwayStationName = railwayStationName;
    }
    
    /**
     * Get the value of railwayStationDTO
     *
     * @return the value of railwayStationDTO
     */
    public RailwayStationDTO getRailwayStationDTO() {
        return railwayStationDTO;
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
     * Set the value of railwayNetworkName
     *
     * @param railwayNetworkName new value of railwayNetworkName
     */
    public void setRailwayNetworkName(String railwayNetworkName) {
        this.railwayNetworkName = railwayNetworkName;
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
        try {
            List<RailwayNetwork> railwayNetwork = railwayNetworkDAO.fetchByNameWithCollections(railwayNetworkName, "railwayStations");
            RailwayStation railwayStation = new RailwayStation();
            railwayStation.setName(railwayStationName);
            railwayNetwork.get(0).addRailwayStation(railwayStation);
            railwayNetworkDAO.makePersistent(railwayNetwork.get(0));
            railwayStationDTO = new RailwayStationDTO(railwayStationDAO.findByName(railwayStationName).get(0));
            return SUCCESS;
        } catch(EmptyResultDataAccessException erdae) {
            errorMessage = getText("addStation.EmptyResultDataAccessException");
            return ERROR;
        } catch (DataIntegrityViolationException dive) {
            errorMessage = getText("addStation.dataIntegrityViolationException");
            return ERROR;
        } catch(DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        }
    }   
}
