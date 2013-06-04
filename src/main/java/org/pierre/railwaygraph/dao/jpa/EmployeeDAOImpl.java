/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao.jpa;

import org.pierre.railwaygraph.domain.Employee;
import org.pierre.railwaygraph.dao.EmployeeDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pierre
 */
@Repository
@Transactional(propagation=Propagation.REQUIRED)
public class EmployeeDAOImpl extends GenericJpaDAO<Employee, Long> implements EmployeeDAO {

    public EmployeeDAOImpl() {
        super();
    }
    
    
    public <T extends Employee> T findByUsername(String name) throws InstantiationException, IllegalAccessException {
        T specificEmploye = (T) getEntityType().newInstance();
        
        specificEmploye = (T) getEntityManager().createQuery("SELECT e FROM " + getEntityType().getName() 
                + " e WHERE e.username LIKE :name")
            .setParameter("name", name).getSingleResult();
        return specificEmploye;
    }
    
    public <T extends Employee> T fetchByUsernameWithCollections(String username, String... collectionNames) throws InstantiationException, IllegalAccessException {
        T specificEmploye = (T) getEntityType().newInstance();
        
        String queryString = "SELECT e FROM " + getEntityType().getName() + " e";
        for(String s: collectionNames) {
            queryString += " left join fetch e." + s;
        }
        queryString += " WHERE e.username LIKE :name";
        specificEmploye = (T) getEntityManager().createQuery(queryString)
            .setParameter("name", username).getSingleResult();
        return specificEmploye;
    }
}
