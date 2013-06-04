/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.section;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.List;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.SectionDAO;
import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.dao.RailwayNetworkDAO;
import org.pierre.railwaygraph.domain.Section;
import org.pierre.railwaygraph.domain.RailwayStation;
import org.pierre.railwaygraph.domain.RailwayNetwork;
import org.pierre.railwaygraph.domain.dto.SectionDTO;
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
public class AddSectionAction extends BaseAction {
    
        private String sectionName;
        private String sectionNbkms;
        private SectionDTO sectionDTO;
        private SectionDAO sectionDAO;
        private RailwayNetworkDAO railwayNetworkDAO;
        private String railwayNetworkName;
        private RailwayStationDAO railwayStationDAO;
        private String departureStationId;
        private String arrivalStationId;
        private String errorMessage = "";



    /**
     * Set the value of sectionName
     *
     * @param sectionName new value of sectionName
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
    
    /**
     * Set the value of sectionNbkms
     *
     * @param sectionNbkms new value of sectionNbkms
     */
    public void setSectionNbkms(String sectionNbkms) {
        this.sectionNbkms = sectionNbkms;
    }
    
    /**
     * Get the value of sectionDTO
     *
     * @return the value of sectionDTO
     */
    public SectionDTO getSectionDTO() {
        return sectionDTO;
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
     * Set the value of railwayStationDAO
     *
     * @param railwayStationDAO new value of railwayStationDAO
     */
    public void setRailwayStationDAO(RailwayStationDAO railwayStationDAO) {
        this.railwayStationDAO = railwayStationDAO;
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
        String[] tab = departureStationId.split("_");
        departureStationId = tab[1];
        String[] tab2 = arrivalStationId.split("_");
        arrivalStationId = tab2[1];
        try {
            List<RailwayNetwork> railwayNetwork = railwayNetworkDAO.fetchByNameWithCollections(railwayNetworkName, "sections");
            RailwayStation departureStation = railwayStationDAO.findById(Long.valueOf(departureStationId));
            RailwayStation arrivalStation = railwayStationDAO.findById(Long.valueOf(arrivalStationId));
            Section section = new Section();
            section.setName(sectionName);
            section.setNbKms(Float.valueOf(sectionNbkms));
            departureStation.addDepartureSection(section);
            arrivalStation.addArrivalSection(section);
            railwayNetwork.get(0).addSection(section);
            railwayNetworkDAO.makePersistent(railwayNetwork.get(0));
            sectionDTO = new SectionDTO(sectionDAO.findByName(sectionName).get(0));
            return SUCCESS;
        } catch(EmptyResultDataAccessException erdae) {
            errorMessage = getText("addSection.EmptyResultDataAccessException");
            return ERROR;
        } catch (DataIntegrityViolationException dive) {
            errorMessage = getText("addSection.dataIntegrityViolationException");
            return ERROR;
        } catch (DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        } catch (BusinessRuleException bre) {
            errorMessage = bre.getMessage();
            return ERROR;
        }
    }
    
    

    
}
