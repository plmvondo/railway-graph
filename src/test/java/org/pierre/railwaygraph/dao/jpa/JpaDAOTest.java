/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.dao.jpa;

import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pierre.railwaygraph.dao.SectionDAO;
import org.pierre.railwaygraph.dao.EmployeeDAO;
import org.pierre.railwaygraph.dao.RailwayStationDAO;
import org.pierre.railwaygraph.dao.RailwayNetworkDAO;
import org.pierre.railwaygraph.dao.PathDAO;
import org.pierre.railwaygraph.domain.Address;
import org.pierre.railwaygraph.domain.Section;
import org.pierre.railwaygraph.domain.PathSection;
import org.pierre.railwaygraph.domain.RailwayStation;
import org.pierre.railwaygraph.domain.RailwayNetworkManager;
import org.pierre.railwaygraph.domain.RailwayNetwork;
import org.pierre.railwaygraph.domain.Path;
import org.pierre.railwaygraph.util.CharSequenceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Pierre
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:applicationContext.xml"
        })
@ActiveProfiles("jpa")
public class JpaDAOTest {
   
    
    @Autowired
    private RailwayNetworkDAO railwayNetworkDAO;
    @Autowired
    private RailwayStationDAO railwayStationDAO;
    @Autowired
    private SectionDAO sectionDAO;
    @Autowired
    private EmployeeDAO employeeDAO;
    @Autowired
    private PathDAO pathDAO;
    
    
    @Test
    public void insertRailwayNetwork() {
        RailwayNetwork rn = new RailwayNetwork();
        rn.setName("Réseau ferré de France");
        railwayNetworkDAO.makePersistent(rn);
        List<RailwayNetwork> r1 = railwayNetworkDAO.findAll();
        assertEquals(1, r1.size());
        assertEquals(rn.getName(), r1.get(0).getName());
    }
    
    @Test
    public void addStationToRailwayNetworkWithCascade() {
        RailwayStation g = new RailwayStation();
        g.setName("Lyon");
        RailwayStation g2 = new RailwayStation();
        g2.setName("Clermont-Ferrand");
        RailwayStation g3 = new RailwayStation();
        g3.setName("Bordeaux");
        RailwayStation g4 = new RailwayStation();
        g4.setName("Orléans");
        
        String[] collectionNames = {"railwayStations"};
        List<RailwayNetwork> rn = railwayNetworkDAO.fetchByNameWithCollections("Réseau ferré de France", collectionNames);
        assertEquals(1, rn.size());
        rn.get(0).addRailwayStation(g);
        rn.get(0).addRailwayStation(g2);
        rn.get(0).addRailwayStation(g3);
        rn.get(0).addRailwayStation(g4);
        railwayNetworkDAO.makePersistent(rn.get(0));
        
        List<RailwayStation> railwayStations = railwayStationDAO.findAll();
        assertEquals(4, railwayStations.size());
        List<RailwayStation> OrleansStation = railwayStationDAO.findByName("Orléans");
        assertNotNull(OrleansStation.get(0).getId());
        assertNotNull(OrleansStation.get(0).getCreated());
        assertEquals(g4.getName(), OrleansStation.get(0).getName());
        assertEquals(4, rn.get(0).getRailwayStations().size());
    }
    
    @Test
    public void addSectionToRailwayNetworkWithCascade() {
        List<RailwayNetwork> rn = railwayNetworkDAO.fetchByNameWithCollections("Réseau ferré de France", "sections");
        
        String[] stationCollectionNames = {"departureSections", "arrivalSections"};
        List<RailwayStation> LyonStation = railwayStationDAO.fetchByNameWithCollections("Lyon", stationCollectionNames);
        List<RailwayStation> CFStation = railwayStationDAO.fetchByNameWithCollections("Clermont-Ferrand", stationCollectionNames);
        List<RailwayStation> BordeauxStation = railwayStationDAO.fetchByNameWithCollections("Bordeaux", stationCollectionNames);
        List<RailwayStation> OrleansStation = railwayStationDAO.fetchByNameWithCollections("Orléans", stationCollectionNames);
        
        assertEquals(1, rn.size());
        assertEquals(1, LyonStation.size());
        assertEquals(1, CFStation.size());
        Section a = new Section();
        a.setName("Lyon_Clermont-Ferrand_1");
        a.setNbKms(new Float(135.78));
        CFStation.get(0).addArrivalSection(a);
        LyonStation.get(0).addDepartureSection(a);
    
        Section b = new Section();
        b.setName("Clermont-Ferrand_Bordeaux_1");
        b.setNbKms(new Float(305.45));
        CFStation.get(0).addDepartureSection(b);
        BordeauxStation.get(0).addArrivalSection(b);

        Section c = new Section();
        c.setName("Lyon_Orléans_1");
        c.setNbKms(new Float(326.08));
        LyonStation.get(0).addDepartureSection(c);
        OrleansStation.get(0).addArrivalSection(c);

        Section d = new Section();
        d.setName("Orléans_Bordeaux_1");
        d.setNbKms(new Float(391.05));
        OrleansStation.get(0).addDepartureSection(d);
        BordeauxStation.get(0).addArrivalSection(d);

        rn.get(0).addSection(a);
        rn.get(0).addSection(b);
        rn.get(0).addSection(c);
        rn.get(0).addSection(d);
        railwayNetworkDAO.makePersistent(rn.get(0));
        
        List<Section> section = sectionDAO.findByName("Clermont-Ferrand_Bordeaux_1");
        assertNotNull(section.get(0).getId());
        assertNotNull(section.get(0).getCreated());
        assertEquals(rn.get(0).getId(), section.get(0).getRailwayNetwork().getId());
        assertEquals(4, rn.get(0).getSections().size());    
        assertEquals(2, LyonStation.get(0).getDepartureSections().size());
        assertEquals(1, OrleansStation.get(0).getDepartureSections().size());
        assertEquals(1, OrleansStation.get(0).getArrivalSections().size());
    }
    
    @Test
    public void addPathToRailwayNetworkWithCascade() {
        String[] railwayCollectionNames = {"sections", "paths"};
        List<RailwayNetwork> rn = railwayNetworkDAO.fetchByNameWithCollections("Réseau ferré de France", railwayCollectionNames);
        
        String[] stationCollectionNames = {"departurePaths", "arrivalPaths"};
        List<RailwayStation> LyonStation = railwayStationDAO.fetchByNameWithCollections("Lyon", stationCollectionNames);
        List<RailwayStation> BordeauxStation = railwayStationDAO.fetchByNameWithCollections("Bordeaux", stationCollectionNames);
        
        Path p = new Path();
        p.setName("Lyon-Bordeaux_1");
        LyonStation.get(0).addDeparturePath(p);
        BordeauxStation.get(0).addArrivalPath(p);
        Path p2 = new Path();
        p2.setName("Lyon-Bordeaux_2");
        LyonStation.get(0).addDeparturePath(p2);
        BordeauxStation.get(0).addArrivalPath(p2);
        
        rn.get(0).addPath(p);
        rn.get(0).addPath(p2);
        railwayNetworkDAO.makePersistent(rn.get(0));
        
        List<Path> path = pathDAO.findByName("Lyon-Bordeaux_1");
        List<Path> path2 = pathDAO.findByName("Lyon-Bordeaux_2");
        assertEquals(p.getName(), path.get(0).getName());
        assertEquals(p2.getName(), path2.get(0).getName());
        assertNotNull(path.get(0).getCreated());
        assertEquals(rn.get(0).getId(), path.get(0).getRailwayNetwork().getId());
        assertEquals(2, rn.get(0).getPaths().size());
        
        assertEquals(2, LyonStation.get(0).getDeparturePaths().size());
        assertEquals(2, BordeauxStation.get(0).getArrivalPaths().size());
    }
    
    @Test
    public void addSectionToPath() {
        
        List<Section> section = sectionDAO.findByName("Lyon_Clermont-Ferrand_1");
        List<Section> section2 = sectionDAO.findByName("Clermont-Ferrand_Bordeaux_1");
        List<Section> section3 = sectionDAO.findByName("Lyon_Orléans_1");
        List<Section> section4 = sectionDAO.findByName("Orléans_Bordeaux_1");
        assertEquals(1, section.size());
        assertEquals(1, section2.size());
        assertEquals(1, section3.size());
        assertEquals(1, section4.size());
        
        String[] collectionNames2 = {"servedStations"};
        List<Path> path = pathDAO.fetchByNameWithCollections("Lyon-Bordeaux_1", collectionNames2);
        List<Path> path2 = pathDAO.fetchByNameWithCollections("Lyon-Bordeaux_2", collectionNames2);
        assertEquals(1, path.size());
        assertEquals(1, path2.size());
        
        assertEquals(0, path.get(0).getPathSection().size());
        
        PathSection ps = new PathSection(section.get(0), path.get(0), true, true);
        PathSection ps2 = new PathSection(section2.get(0), path.get(0), true, true);
        path.get(0).addPathSection(ps);
        path.get(0).addPathSection(ps2);
        pathDAO.makePersistent(path.get(0));
        
        PathSection ps3 = new PathSection(section3.get(0), path2.get(0), true, true);
        PathSection ps4 = new PathSection(section4.get(0), path2.get(0), true, true);
        path2.get(0).addPathSection(ps3);
        path2.get(0).addPathSection(ps4);
        pathDAO.makePersistent(path2.get(0));
        
        assertEquals(2, path.get(0).getPathSection().size());
        assertEquals(2, path2.get(0).getPathSection().size());
        assertEquals(true, path2.get(0).getPathSection().get(0).isArrivalStationServed());
        
        assertEquals(ps3.getSection().getName(), path2.get(0).getPathSection().get(0).getSection().getName());
        
        Float r = section.get(0).getNbKms() + section2.get(0).getNbKms();
        Float rTotal = path.get(0).getNbKms();
        assertEquals(r, rTotal);
       
        Float r2 = section3.get(0).getNbKms() + section4.get(0).getNbKms();
        Float rTotal2 = path2.get(0).getNbKms();
        assertEquals(r2, rTotal2);
        assertEquals(3, path.get(0).getServedStations().size());
        assertEquals(3, path2.get(0).getServedStations().size());
    }
    
    @Test
    public void addRailwayNetworkManagerWithCascade() throws InstantiationException, IllegalAccessException {
        RailwayNetworkManager rnm = new RailwayNetworkManager();
        rnm.setUsername("eledru");
        String pwd1 = new StandardPasswordEncoder().encode(new CharSequenceImpl("48uhgrf;dcf42;@"));
        rnm.setPassword(pwd1);
        rnm.setAddress(new Address("25, rue Bonaparte", "Résidence les Aubes", "75015",
                "Paris", "France"));
        rnm.setHireDate(new Date());
        rnm.setLastname("Ledru");
        rnm.setSocialSecurityNumber("162091315478445");
        rnm.setFirstname("Eric");
        rnm.setEnabledStatus(true);
        rnm.setGrantedAuthority("ROLE_GEST");
        
        RailwayNetworkManager rnm2 = new RailwayNetworkManager();
        rnm2.setUsername("ulegrand");
        String pwd2 = new StandardPasswordEncoder().encode(new CharSequenceImpl("hgrf53;dfAERTgv21o"));
        rnm2.setPassword(pwd2);
        rnm2.setAddress(new Address("30, rue Leconte", "Résidence les Saules", "45000",
                "Orléans", "France"));
        rnm2.setHireDate(new Date());
        rnm2.setLastname("Legrand");
        rnm2.setSocialSecurityNumber("188114517439747");
        rnm2.setFirstname("Ulysse");
        rnm2.setEnabledStatus(true);
        rnm2.setGrantedAuthority("ROLE_GEST");
        
        rnm2.addWorker(rnm);
        employeeDAO.makePersistent(rnm2);
        
        String[] collectionNames = {"workers"};
        RailwayNetworkManager railwayNetworkManager1 = employeeDAO.fetchByUsernameWithCollections("eledru", collectionNames);
        RailwayNetworkManager railwayNetworkManager2 = employeeDAO.fetchByUsernameWithCollections("ulegrand", collectionNames);
        assertEquals(0, railwayNetworkManager1.getWorkers().size());
        assertEquals(1, railwayNetworkManager2.getWorkers().size());
        assertEquals(rnm.getLastname(), railwayNetworkManager1.getLastname());
        assertEquals(rnm2.getLastname(), railwayNetworkManager2.getLastname());
        assertEquals(rnm2.getLastname(), railwayNetworkManager1.getManager().getLastname());
        
    }
    
    @Test
    public void shortestPathTest() {
        
        List<RailwayNetwork> rn = railwayNetworkDAO.findAll();
        List<RailwayStation> LyonStation = railwayStationDAO.findByName("Lyon");
        List<RailwayStation> BordeauxStation = railwayStationDAO.findByName("Bordeaux");
        List<Section> shortestPath = rn.get(0).shortestPath(LyonStation.get(0), BordeauxStation.get(0));
        
        assertEquals(2, shortestPath.size());
        assertEquals("Lyon_Clermont-Ferrand_1", shortestPath.get(0).getName());
        assertEquals("Clermont-Ferrand_Bordeaux_1", shortestPath.get(1).getName());
        
    }
    
}
