/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.section;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.SectionDAO;
import org.pierre.railwaygraph.domain.Section;
import org.pierre.railwaygraph.domain.dto.SectionDTO;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class EditSectionAction extends BaseAction {
    
        private SectionDAO sectionDAO;
        private String sectionId;
        private String sectionName;
        private SectionDTO sectionDTO;
        private String errorMessage = "";



    /**
     * Set the value of sectionDAO
     *
     * @param sectionDAO new value of sectionDAO
     */
    public void setSectionDAO(SectionDAO sectionDAO) {
        this.sectionDAO = sectionDAO;
    }
    
    /**
     * Set the value of sectionId
     *
     * @param sectionId new value of sectionId
     */
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
    
    /**
     * Set the value of sectionName
     *
     * @param sectionName new value of sectionName
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
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
     * Get the value of errorMessage
     *
     * @return the value of errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    

    @Override
    public String execute() throws Exception {
        String[] tab = sectionId.split("_");
        sectionId = tab[1];
        try {
            Section section = sectionDAO.findById(Long.valueOf(sectionId));
            section.setName(sectionName);
            sectionDAO.makePersistent(section);
            sectionDTO = new SectionDTO(section);
            return SUCCESS;
        } catch(EmptyResultDataAccessException erde) {
            errorMessage = getText("section.EmptyResultDataAccessException");
            return ERROR;
        } catch(DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        }
    }
       
}
