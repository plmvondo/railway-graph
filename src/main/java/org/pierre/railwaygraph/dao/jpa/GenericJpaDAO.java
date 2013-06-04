/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.pierre.railwaygraph.dao.GenericDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pierre
 */
@Repository
@Transactional(propagation=Propagation.REQUIRED)
public abstract class GenericJpaDAO<T, ID extends Serializable> implements  GenericDAO<T, ID> {
    
    private Class<T> entityType;
    
    @PersistenceContext
    private EntityManager entityManager;

    
    public GenericJpaDAO() {
        this.entityType = (Class<T>) ((ParameterizedType) getClass()
                                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
        
         
        

    /**
     * Get the value of entityType
     *
     * @return the value of entityType
     */
    public Class<T> getEntityType() {
        return entityType;
    }
    
    

    /**
     * Get the value of entityManager
     *
     * @return the value of entityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Set the value of entityManager
     *
     * @param entityManager new value of entityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public T findById(ID id) {
        T entity;
        entity = getEntityManager().find(getEntityType(), id);
        return entity;
    }

    @Override
    public List<T> findAll() {
        return getEntityManager().createQuery("FROM " + getEntityType().getName() ).getResultList();
    }
    
    @Override
    public List<T> findByName(String name) {
        return getEntityManager().createQuery("SELECT e FROM " + getEntityType().getName() 
                + " e WHERE e.name LIKE :name")
            .setParameter("name", name).setMaxResults(10).getResultList();
    }

    public T fetchByIdWithCollections(ID id, String... collectionNames) {
        String queryString = "SELECT e FROM " + getEntityType().getName() + " e";
        for(String s: collectionNames) {
            queryString += " left join fetch e." + s;
        }
        queryString += " WHERE e.id = :id";
        
        return (T) getEntityManager().createQuery(queryString)
            .setParameter("id", id).getSingleResult();
    }
    
    
    
    public List<T> fetchByNameWithCollections(String name, String... collectionNames) {
        String queryString = "SELECT e FROM " + getEntityType().getName() + " e";
        for(String s: collectionNames) {
            queryString += " left join fetch e." + s;
        }
        queryString += " WHERE e.name LIKE :name";
        
        return getEntityManager().createQuery(queryString)
            .setParameter("name", name).setMaxResults(10).getResultList();
    }

    public List<T> fetchWithCollections(String... collectionNames) {
        String queryString = "SELECT e FROM " + getEntityType().getName() + " e";
        for(String s: collectionNames) {
            queryString += " left join fetch e." + s;
        }
        return getEntityManager().createQuery(queryString).setMaxResults(10).getResultList();
    }
    
    
    

    @Override
    public T makePersistent(T entity) {
        return getEntityManager().merge(entity);
    }

    @Override
    public void makeTransient(ID id) {
        T obj = this.findById(id);
        getEntityManager().remove(obj);
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }
    

    @Override
    public void clear() {
        getEntityManager().clear();
    }
    
}
