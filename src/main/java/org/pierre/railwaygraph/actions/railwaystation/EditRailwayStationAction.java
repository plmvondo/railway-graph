/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.railwaystation;

import static com.opensymphony.xwork2.Action.ERROR;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.domain.RailwayStation;
import org.pierre.railwaygraph.domain.dto.RailwayStationDTO;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class EditRailwayStationAction extends BaseAction {
    
        private RailwayStationDAO railwayStationDAO;
        private String railwayStationId;
        private String railwayStationName;
        private RailwayStationDTO railwayStationDTO;
        private String errorMessage = "";

    
    /**
     * Set the value of railwayStationDAO
     *
     * @param railwayStationDAO new value of railwayStationDAO
     */
    public void setRailwayStationDAO(RailwayStationDAO railwayStationDAO) {
        this.railwayStationDAO = railwayStationDAO;
    }
    
    /**
     * Set the value of railwayStationId
     *
     * @param railwayStationId new value of railwayStationId
     */
    public void setRailwayStationId(String railwayStationId) {
        this.railwayStationId = railwayStationId;
    }
    
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
     * Get the value of errorMessage
     *
     * @return the value of errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    

    @Override
    public String execute() throws Exception {
        String[] tab = railwayStationId.split("_");
        railwayStationId = tab[1];
        try {
            RailwayStation station = railwayStationDAO.findById(Long.valueOf(railwayStationId));
            station.setName(railwayStationName);
            railwayStationDAO.makePersistent(station);
            railwayStationDTO = new RailwayStationDTO(station);
            return SUCCESS;
        } catch(EmptyResultDataAccessException erde) {
            errorMessage = getText("station.EmptyResultDataAccessException");
            return ERROR;
        } catch(DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        }
    } 
}
