/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.exceptions;

/**
 *
 * @author Pierre
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException() {
    }
    

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessRuleException(Throwable cause) {
        super(cause);
    }
    
    
}
