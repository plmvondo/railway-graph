/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao.jpa;

import org.pierre.railwaygraph.dao.SectionDAO;
import org.pierre.railwaygraph.domain.Section;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pierre
 */
@Repository
@Transactional(propagation=Propagation.REQUIRED)
public class SectionDAOImpl extends GenericJpaDAO<Section, Long> implements SectionDAO {

    public SectionDAOImpl() {
        super();
    }
    
    
}
