/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions;

import static com.opensymphony.xwork2.Action.SUCCESS;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.pierre.railwaygraph.dao.EmployeeDAO;
import org.pierre.railwaygraph.domain.RailwayNetworkManager;
import org.pierre.railwaygraph.util.CharSequenceImpl;
import org.pierre.railwaygraph.util.SecurityInterceptor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 *
 * @author Pierre
 */
public class LoginAuthAction extends BaseAction implements ServletRequestAware  {
    
        private String username;
        private String password;
        private EmployeeDAO employeeDAO;
        private HttpServletRequest request;
        private String request_locale;
        private String errorMessage = "";

        
    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    
    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the value of employeeDAO
     *
     * @param employeeDAO new value of employeeDAO
     */
    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }
    
    /**
     * Get the value of request_locale
     *
     * @return the value of request_locale
     */
    public String getRequest_locale() {
        return request_locale;
    }

    /**
     * Set the value of request_locale
     *
     * @param request_locale new value of request_locale
     */
    public void setRequest_locale(String request_locale) {
        this.request_locale = request_locale;
    }
    
    /**
     * Get the value of errorMessage
     *
     * @return the value of errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the value of errorMessage
     *
     * @param errorMessage new value of errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    

    @Override
    public String execute() throws Exception {
        if (username == null || username.equals("")) {
            errorMessage = getText("auth.failed");
            return ERROR;
        }
        try {
            RailwayNetworkManager rnm = employeeDAO.findByUsername(username);
            if (new StandardPasswordEncoder().matches(new CharSequenceImpl(password), rnm.getPassword())) {
                request.getSession(true).setAttribute(SecurityInterceptor.USER_OBJECT,rnm);
                return SUCCESS;
            }
            else {
                errorMessage = getText("authPassword.failed");
                return ERROR;
            }
        } catch(EmptyResultDataAccessException erde) {
            errorMessage = getText("authUsername.failed");
            return ERROR;
        }
    }
            
        
}
