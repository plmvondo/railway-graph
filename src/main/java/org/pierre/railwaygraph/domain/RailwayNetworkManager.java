/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Pierre
 */
@Entity
@Table(name = "RAILWAY_NETWORK_MANAGER")
@PrimaryKeyJoinColumn(name = "RAILWAY_NETWORK_MANAGER_ID")
public class RailwayNetworkManager extends Employee {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private boolean enabledStatus;
    private String grantedAuthority;

        
        
    public RailwayNetworkManager() {
        super();
    }
    
    
    public RailwayNetworkManager(String username, String password, String socialSecurityNumber, String lastName, String firstName, String phoneNumber, Date hireDate, Address address) {
        super(socialSecurityNumber, lastName, firstName, phoneNumber, hireDate, address);
        this.username = username;
        this.password = password;
    }
        

    /**
     * Get the value of username
     *
     * @return the value of username
     */
        @Column(name = "USERNAME", length = 30, nullable = false, unique = true)
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
    @Column(name = "PASSWORD", nullable = false)
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
     * Get the value of enabledStatus
     *
     * @return the value of enabledStatus
     */
    @Column(name = "ENABLED_STATUS", nullable = false)
    public boolean isEnabledStatus() {
        return enabledStatus;
    }

    /**
     * Set the value of enabledStatus
     *
     * @param enabledStatus new value of enabledStatus
     */
    public void setEnabledStatus(boolean enabledStatus) {
        this.enabledStatus = enabledStatus;
    }
    

    /**
     * Get the value of grantedAuthority
     *
     * @return the value of grantedAuthority
     */
    @Column(name = "GRANTED_AUTHORITY", nullable = false)
    public String getGrantedAuthority() {
        return grantedAuthority;
    }

    /**
     * Set the value of grantedAuthority
     *
     * @param grantedAuthority new value of grantedAuthority
     */
    public void setGrantedAuthority(String grantedAuthority) {
        this.grantedAuthority = grantedAuthority;
    }


    

    @Override
    public String toString() {
        return super.toString() + ", Username: '" + getUsername() + "'";
    }
    
}
