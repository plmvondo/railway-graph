/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.path;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.PathDAO;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class DeletePathAction extends BaseAction {
    
        private String pathId;
        private PathDAO pathDAO;
        private String errorMessage = "";
        
        
    /**
     * Get the value of pathId
     *
     * @return the value of pathId
     */
    public String getPathId() {
        return pathId;
    }



    /**
     * Set the value of pathId
     *
     * @param pathId new value of pathId
     */
    public void setPathId(String pathId) {
        this.pathId = pathId;
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
     * Get the value of errorMessage
     *
     * @return the value of errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    

    @Override
    public String execute() throws Exception {
        String[] tab = pathId.split("_");
        pathId = tab[1];
        try {
            pathDAO.makeTransient(Long.valueOf(pathId));
            pathId = "path_" + pathId;
            return SUCCESS;
        } catch(EmptyResultDataAccessException erde) {
            errorMessage = getText("path.EmptyResultDataAccessException");
            return ERROR;
        } catch (DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        }
    }
    
    

    
}
