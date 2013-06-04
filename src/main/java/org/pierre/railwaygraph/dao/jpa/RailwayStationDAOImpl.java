/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao.jpa;

import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.domain.RailwayStation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pierre
 */
@Repository
@Transactional(propagation=Propagation.REQUIRED)
public class RailwayStationDAOImpl extends GenericJpaDAO<RailwayStation,Long> implements RailwayStationDAO {

    public RailwayStationDAOImpl() {
        super();
    }
    
    
}
