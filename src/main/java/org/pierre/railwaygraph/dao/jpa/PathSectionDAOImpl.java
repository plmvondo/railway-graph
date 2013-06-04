/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao.jpa;

import org.pierre.railwaygraph.dao.PathSectionDAO;
import org.pierre.railwaygraph.domain.PathSection;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pierre
 */
@Repository
@Transactional(propagation=Propagation.REQUIRED)
public class PathSectionDAOImpl extends GenericJpaDAO<PathSection, Long> implements PathSectionDAO {

    public PathSectionDAOImpl() {
        super();
    }

    public PathSection findByIds(Long sectionId, Long pathId) {
        return (PathSection) getEntityManager().createQuery("SELECT e FROM PathSection e WHERE e.sectionId = :sectionId"
                + "AND e.pathId = :pathId")
            .setParameter("sectionId", sectionId).setParameter("pathId", pathId).getSingleResult();
    }
    
}
