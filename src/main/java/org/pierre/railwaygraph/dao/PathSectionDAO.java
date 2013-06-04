/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao;

import org.pierre.railwaygraph.domain.PathSection;

/**
 *
 * @author Pierre
 */
public interface PathSectionDAO extends GenericDAO<PathSection, Long> {
    
    PathSection findByIds(Long sectionId, Long pathId);
    
}
