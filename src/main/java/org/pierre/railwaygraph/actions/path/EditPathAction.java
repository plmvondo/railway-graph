/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.path;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.dao.PathDAO;
import org.pierre.railwaygraph.domain.Path;
import org.pierre.railwaygraph.domain.dto.PathDTO;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class EditPathAction extends BaseAction {
    
        private PathDAO pathDAO;
        private String pathId;
        private String pathName;
        private PathDTO pathDTO;
        private String errorMessage = "";


    /**
     * Set the value of pathDAO
     *
     * @param pathDAO new value of pathDAO
     */
    public void setPathDAO(PathDAO pathDAO) {
        this.pathDAO = pathDAO;
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
     * Set the value of pathName
     *
     * @param pathName new value of pathName
     */
    public void setPathName(String pathName) {
        this.pathName = pathName;
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
        String[] tab = pathId.split("_");
        pathId = tab[1];
        try {
            Path path = pathDAO.findById(Long.valueOf(pathId));
            path.setName(pathName);
            pathDAO.makePersistent(path);
            pathDTO = new PathDTO(path);
            return SUCCESS;
        } catch(EmptyResultDataAccessException erde) {
            errorMessage = getText("path.EmptyResultDataAccessException");
            return ERROR;
        } catch(DataAccessException dae) {
            errorMessage = getText("generic.dataAccessException");
            return ERROR;
        }
    }
}
