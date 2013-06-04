/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.actions.treesearch;

import com.opensymphony.xwork2.Action;
import java.util.ArrayList;
import org.apache.struts2.StrutsSpringTestCase;
import static org.easymock.EasyMock.*;
import org.junit.Before;
import org.junit.Test;
import org.pierre.railwaygraph.dao.PathDAO;
import org.pierre.railwaygraph.dao.RailwayNetworkDAO;
import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.dao.SectionDAO;
import org.pierre.railwaygraph.domain.Path;
import org.pierre.railwaygraph.domain.RailwayNetwork;
import org.pierre.railwaygraph.domain.RailwayStation;
import org.pierre.railwaygraph.domain.Section;

/**
 *
 * @author Pierre
 */
public class TreeStoreActionTest extends StrutsSpringTestCase {
    
    private TreeStoreAction treeStoreAction;
    private RailwayNetworkDAO railwayNetworkDAOMock;
    private RailwayStationDAO railwayStationDAOMock;
    private SectionDAO sectionDAOMock;
    private PathDAO pathDAOMock;
    
    @Before
    @Override
    public void setUp() {
        railwayNetworkDAOMock = createMock(RailwayNetworkDAO.class);
        railwayStationDAOMock = createMock(RailwayStationDAO.class);
        sectionDAOMock = createMock(SectionDAO.class);
        pathDAOMock = createMock(PathDAO.class);
        treeStoreAction = new TreeStoreAction();
        treeStoreAction.setRailwayNetworkDAO(railwayNetworkDAOMock);
        treeStoreAction.setRailwayStationDAO(railwayStationDAOMock);
        treeStoreAction.setSectionDAO(sectionDAOMock);
        treeStoreAction.setPathDAO(pathDAOMock);
    }
    
    @Test
    public void testTreeStoreAction() throws Exception {
        expect(railwayNetworkDAOMock.findByName("Réseau ferré de France")).andReturn(new ArrayList<RailwayNetwork>());
        expectLastCall().times(1);
        expect(railwayStationDAOMock.findAll()).andReturn(new ArrayList<RailwayStation>());
        expectLastCall().times(1);
        expect(sectionDAOMock.findAll()).andReturn(new ArrayList<Section>());
        expectLastCall().times(1);
        expect(pathDAOMock.findAll()).andReturn(new ArrayList<Path>());
        expectLastCall().times(1);
        replay(railwayNetworkDAOMock);
        replay(railwayStationDAOMock);
        replay(sectionDAOMock);
        replay(pathDAOMock);
        assertEquals(Action.SUCCESS, treeStoreAction.execute());
        verify(railwayNetworkDAOMock);
        verify(railwayStationDAOMock);
        verify(sectionDAOMock);
        verify(pathDAOMock);
    }
}
