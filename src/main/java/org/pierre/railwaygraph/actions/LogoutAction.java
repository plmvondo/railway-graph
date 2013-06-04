/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.pierre.railwaygraph.util.MandatoryAuthentication;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class LogoutAction extends BaseAction implements ServletRequestAware {
    
    private HttpServletRequest request;
    
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String execute() throws Exception {
        request.getSession().invalidate();
        return SUCCESS;
    } 

}
