/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.util;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.pierre.railwaygraph.domain.RailwayNetworkManager;

/**
 *
 * @author Pierre
 */
public class SecurityInterceptor extends AbstractInterceptor {
    
    public static final String USER_OBJECT = "user";
    public static final String LOGIN_RESULT = "authenticate";

    public static final String ERROR_MSG_KEY = "msg.pageRequiresRegistration";
    public static final String DEFAULT_MSG = "This page requires registration, please logon or register";

    private List<String> mandatoryAuthentication;

    public void setMandatoryAuthentication( String authenticate ) {
        this.mandatoryAuthentication = stringToList(authenticate);
    }

    public String intercept(ActionInvocation invocation) throws Exception {

        RailwayNetworkManager user = (RailwayNetworkManager)invocation.getInvocationContext().getSession().get(USER_OBJECT);
        Object action = invocation.getAction();
        boolean annotated = action.getClass().isAnnotationPresent(MandatoryAuthentication.class);

        if( user == null &&
                ( annotated || mandatoryAuthentication(invocation.getProxy().getNamespace()) ) ) {
            if( action instanceof ValidationAware) {
                String msg = action instanceof TextProvider ?
                        ((TextProvider)action).getText(ERROR_MSG_KEY) : DEFAULT_MSG;
                ((ValidationAware)action).addActionError(msg);
            }
            return LOGIN_RESULT;
        } else {
            
        }

        return invocation.invoke();
    }

    private List<String> stringToList(String val) {
        if (val != null) {
            String[] list = val.split("[ ]*,[ ]*");
            return Arrays.asList(list);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    private boolean mandatoryAuthentication( String namespace ) {
        for (Iterator<String> it = mandatoryAuthentication.iterator(); it.hasNext();) {
            String next = it.next();
            if( namespace.equals(next.trim()) ) {
                return true;
            }
        }
        return false;
    }
    
}
