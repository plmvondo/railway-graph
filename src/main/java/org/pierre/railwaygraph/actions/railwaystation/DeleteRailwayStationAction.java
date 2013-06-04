/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.railwaystation;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class DeleteRailwayStationAction extends BaseAction {
        
        private String railwayStationId;
        private RailwayStationDAO railwayStationDAO;
        private String errorMessage = "";


    /**
     * Get the value of railwayStationId
     *
     * @return the value of railwayStationId
     */
    public String getRailwayStationId() {
        return railwayStationId;
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
     * Set the value of railwayStationDAO
     *
     * @param railwayStationDAO new value of railwayStationDAO
     */
    public void setRailwayStationDAO(RailwayStationDAO railwayStationDAO) {
        this.railwayStationDAO = railwayStationDAO;
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
            railwayStationDAO.makeTransient(Long.valueOf(railwayStationId));
            railwayStationId = "station_" + railwayStationId;
            return SUCCESS;
        } catch(EmptyResultDataAccessException erde) {
            errorMessage = getText("station.EmptyResultDataAccessException");
            return ERROR;
        } catch (DataIntegrityViolationException dive) {
            errorMessage = getText("deleteStation.dataIntegrityViolationException");
            return ERROR;
        }
        catch (DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        }
    }
}
