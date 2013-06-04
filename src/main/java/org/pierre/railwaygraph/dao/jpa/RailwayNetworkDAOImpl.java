/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao.jpa;

import org.pierre.railwaygraph.dao.RailwayNetworkDAO;
import org.pierre.railwaygraph.domain.RailwayNetwork;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pierre
 */
@Repository
@Transactional(propagation=Propagation.REQUIRED)
public class RailwayNetworkDAOImpl extends GenericJpaDAO<RailwayNetwork, Long> implements RailwayNetworkDAO {

    public RailwayNetworkDAOImpl() {
        super();
    }
    
    
}
