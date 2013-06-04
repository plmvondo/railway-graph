/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.railwaynetwork;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.pierre.railwaygraph.actions.BaseAction;
import org.pierre.railwaygraph.domain.RailwayNetworkManager;
import org.pierre.railwaygraph.util.MandatoryAuthentication;
import org.pierre.railwaygraph.util.SecurityInterceptor;

/**
 *
 * @author Pierre
 */
@MandatoryAuthentication
public class RailwayNetworkIndexAction extends BaseAction implements ServletRequestAware {
    
        private RailwayNetworkManager railwayNetworkManager;
        private HttpServletRequest request;
        

    /**
     * Get the value of railwayNetworkManager
     *
     * @return the value of railwayNetworkManager
     */
    public RailwayNetworkManager getRailwayNetworkManager() {
        return railwayNetworkManager;
    }

    /**
     * Set the value of railwayNetworkManager
     *
     * @param railwayNetworkManager new value of railwayNetworkManager
     */
    public void setRailwayNetworkManager(RailwayNetworkManager railwayNetworkManager) {
        this.railwayNetworkManager = railwayNetworkManager;
    }
    
    
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String execute() throws Exception {
        railwayNetworkManager = (RailwayNetworkManager) request.getSession(true).getAttribute(SecurityInterceptor.USER_OBJECT);
        return SUCCESS;
    }
  
}
