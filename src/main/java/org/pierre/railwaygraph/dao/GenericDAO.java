/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Pierre
 */
public interface GenericDAO<T, ID extends Serializable> {
    
    T findById(ID id);

    List<T> findAll();
    
    List<T> findByName(String name);
    
    T fetchByIdWithCollections(ID id, String... collectionNames);
    
    List<T> fetchByNameWithCollections(String name, String... collectionNames);
    
    List<T> fetchWithCollections(String... collectionNames);

    T makePersistent(T entity);

    void makeTransient(ID id);
    
    void flush();
    
    void clear();
    
}
