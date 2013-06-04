/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.shortestpath;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.List;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.dao.RailwayNetworkDAO;
import org.pierre.railwaygraph.domain.Section;
import org.pierre.railwaygraph.domain.RailwayStation;
import org.pierre.railwaygraph.domain.RailwayNetwork;
import org.pierre.railwaygraph.domain.dto.SectionDTO;
import org.pierre.railwaygraph.exceptions.BusinessRuleException;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class ShortestPathAction extends BaseAction {
    
        private RailwayNetworkDAO railwayNetworkDAO;
        private RailwayStationDAO railwayStationDAO;
        private List<SectionDTO> shortestPath;
        private String departureStationId;
        private String arrivalStationId;
        private String errorMessage = "";


    /**
     * Set the value of railwayNetworkDAO
     *
     * @param railwayNetworkDAO new value of railwayNetworkDAO
     */
    public void setRailwayNetworkDAO(RailwayNetworkDAO railwayNetworkDAO) {
        this.railwayNetworkDAO = railwayNetworkDAO;
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
     * Get the value of shortestPath
     *
     * @return the value of shortestPath
     */
    public List<SectionDTO> getShortestPath() {
        return shortestPath;
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
            if (departureStationId.equals(arrivalStationId)) {
                throw new BusinessRuleException("The departure and arrival stations are the same !");
            }
            String[] tab = departureStationId.split("_");
            String departureId = tab[1];
            String[] tab2 = arrivalStationId.split("_");
            String arrivalId = tab2[1];
            
            List<RailwayNetwork> railwayNetwork = railwayNetworkDAO.findByName("Réseau ferré de France");
            RailwayStation departureStation = railwayStationDAO.findById(Long.valueOf(departureId));
            RailwayStation arrivalStation = railwayStationDAO.findById(Long.valueOf(arrivalId));
            List<Section> sp = railwayNetwork.get(0).shortestPath(departureStation, arrivalStation);
            shortestPath = new ArrayList<SectionDTO>();
            for(Section path: sp) {
                SectionDTO adto = new SectionDTO(path);
                shortestPath.add(adto);
            }
            return SUCCESS;
        } catch(EmptyResultDataAccessException erde) {
            errorMessage = getText("shortestPath.EmptyResultDataAccessException");
            return ERROR;
        } catch(DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        } catch(BusinessRuleException bre) {
            errorMessage = bre.getMessage();
            return ERROR;
        }
    }
}
