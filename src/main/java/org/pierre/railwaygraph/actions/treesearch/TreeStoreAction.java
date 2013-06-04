/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.treesearch;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.SectionDAO;
import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.dao.RailwayNetworkDAO;
import org.pierre.railwaygraph.dao.PathDAO;
import org.pierre.railwaygraph.domain.Section;
import org.pierre.railwaygraph.domain.RailwayStation;
import org.pierre.railwaygraph.domain.RailwayNetwork;
import org.pierre.railwaygraph.domain.Path;
import org.pierre.railwaygraph.domain.dto.SectionDTO;
import org.pierre.railwaygraph.domain.dto.RailwayStationDTO;
import org.pierre.railwaygraph.domain.dto.RailwayNetworkDTO;
import org.pierre.railwaygraph.domain.dto.PathDTO;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class TreeStoreAction extends BaseAction {
    
        private RailwayNetworkDAO railwayNetworkDAO;
        private RailwayStationDAO railwayStationDAO;
        private SectionDAO sectionDAO;
        private PathDAO pathDAO;
        private List<RailwayNetwork> railwayNetwork;
        private List<RailwayStation> railwayStations;
        private List<Section> sections;
        private List<Path> paths;
        private Map<String, Map<String, String>> gridHeaderNames;
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
     * Set the value of sectionDAO
     *
     * @param sectionDAO new value of sectionDAO
     */
    public void setSectionDAO(SectionDAO sectionDAO) {
        this.sectionDAO = sectionDAO;
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
     * Get the value of gridHeaderNames
     *
     * @return the value of gridHeaderNames
     */
    public Map getGridHeaderNames() {
        return gridHeaderNames;
    }
    
    
    /**
     * Get the value of wrapperDTO
     *
     * @return the value of wrapperDTO
     */
    public List getWrapperDTO() {
        List l = new ArrayList();
        for(RailwayNetwork r: railwayNetwork) {
            l.add(new RailwayNetworkDTO(r));
        }
        l.add(new RailwayStationDTO("_Stations", "Stations", "root"));
        for(RailwayStation g: railwayStations) {
            l.add(new RailwayStationDTO(g));
        }
        l.add(new SectionDTO("_Sections", "Sections", "root"));
        for(Section a: sections) {
            l.add(new SectionDTO(a));
        }
        l.add(new PathDTO("_Paths", "Paths", "root"));
        for(Path t: paths) {
            l.add(new PathDTO(t));
        }
        return l;
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
            railwayNetwork = railwayNetworkDAO.findByName("Réseau ferré de France");
            railwayStations = railwayStationDAO.findAll();
            sections = sectionDAO.findAll();
            paths = pathDAO.findAll();

            gridHeaderNames = new HashMap<String, Map<String, String>>();
            Map railwayStation = new HashMap<String, String>();
            railwayStation.put("id", "Identifier");
            railwayStation.put("name", "Name");
            railwayStation.put("created", "Creation date");
            gridHeaderNames.put("railwayStation", railwayStation);
            Map section = new HashMap<String, String>();
            section.put("id", "Identifier");
            section.put("name", "Name");
            section.put("created", "Creation date");
            section.put("nbKms", "Number of Kms");
            gridHeaderNames.put("section", section);
            Map path = new HashMap<String, String>();
            path.put("id", "Identifier");
            path.put("name", "Name");
            path.put("created", "Creation date");
            path.put("nbKms", "Number of Kms");
            path.put("sections", "List of sections");
            gridHeaderNames.put("path", path);
            return SUCCESS;
        } catch(EmptyResultDataAccessException erdae) {
            errorMessage = getText("treeStore.EmptyResultDataAccessException");
            return ERROR;
        } catch (DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        }
    }
    
}
