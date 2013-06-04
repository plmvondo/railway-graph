/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.section;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.SectionDAO;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class DeleteSectionAction extends BaseAction {
    
        private String sectionId;
        private SectionDAO sectionDAO;
        private String errorMessage = "";

    
    /**
     * Get the value of sectionId
     *
     * @return the value of sectionId
     */
    public String getSectionId() {
        return sectionId;
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
     * Set the value of sectionDAO
     *
     * @param sectionDAO new value of sectionDAO
     */
    public void setSectionDAO(SectionDAO sectionDAO) {
        this.sectionDAO = sectionDAO;
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
            sectionDAO.makeTransient(Long.valueOf(sectionId));
            sectionId = "section_" + sectionId;
            return SUCCESS;
        } catch(EmptyResultDataAccessException erde) {
            errorMessage = getText("section.EmptyResultDataAccessException");
            return ERROR;
        } catch (DataIntegrityViolationException dive) {
            errorMessage = getText("deleteSection.dataIntegrityViolationException");
            return ERROR;
        } catch (DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        }
    }
    
}
