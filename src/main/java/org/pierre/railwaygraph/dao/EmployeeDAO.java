/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao;

import org.pierre.railwaygraph.domain.Employee;

/**
 *
 * @author Pierre
 */
public interface EmployeeDAO extends GenericDAO<Employee, Long> {
    
    <T extends Employee> T findByUsername(String username) throws InstantiationException, IllegalAccessException;
    
    <T extends Employee> T fetchByUsernameWithCollections(String username, String... collectionNames) throws InstantiationException, IllegalAccessException;
}
