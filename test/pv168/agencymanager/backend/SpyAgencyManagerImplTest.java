/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv168.agencymanager.backend;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static pv168.agencymanager.backend.AgentManagerImplTest.assertAgentCollectionDeepEquals;
import static pv168.agencymanager.backend.MissionManagerImplTest.assertMissionCollectionDeepEquals;
import static pv168.agencymanager.backend.MissionManagerImplTest.assertMissionDeepEquals;
import static pv168.agencymanager.backend.MissionManagerImplTest.newMission;
import pv168.common.DBUtils;
import pv168.common.IllegalEntityException;
import pv168.common.ServiceFailureException;

/**
 *
 * @author vlado
 */
public class SpyAgencyManagerImplTest {

    private SpyAgencyManagerImpl manager;
    private AgentManagerImpl agentManager;
    private MissionManagerImpl missionManager;
    private DataSource ds;

    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:spyagency-test;create=true");
        return ds;
    }

    private Date date(String date) {
        return Date.valueOf(date);
    }

    private Mission m1, m2, m3, missionWithNullId, missionNotInDB;
    private Agent a1, a2, a3, a4, a5, agentWithNullId, agentNotInDB;

    private void prepareTestData() throws ServiceFailureException {
        m1 = newMission("Atentat", date("1975-01-01"), true, 1, "note 1");
        m2 = newMission("Sledovacka", date("1975-03-03"), true, 4, "note 2");
        m3 = newMission("Len tak pre radost", date("1975-05-05"), true, 3, "note 2");

        a1 = new Agent(007, "James Bond", false, date("1960-01-01"));
        a2 = new Agent(111, "Duri", false, date("1962-02-02"));
        a3 = new Agent(222, "Fero", false, date("1964-06-06"));
        a4 = new Agent(333, "Mr. Bombastic", false, date("1968-09-09"));
        a5 = new Agent(444, "Erzebet", false, date("1970-12-20"));
        
        agentManager.trainAgent(a1);
        agentManager.trainAgent(a2);
        agentManager.trainAgent(a3);
        agentManager.trainAgent(a4);
        agentManager.trainAgent(a5);
        
        missionManager.createMission(m1);
        missionManager.createMission(m2);
        missionManager.createMission(m3);

        missionWithNullId = newMission("Mission with null id", date("2000-01-01"), true, 2, "note - no id");
        missionNotInDB = newMission("Mission not in DB", date("2012-12-12"), true, 5, "note not in db");
        missionNotInDB.setId(m3.getId() + 100);
        agentWithNullId = new Agent(666, "no id", false, date("1900-01-01"));
        agentNotInDB = new Agent(777, "no in DB", false, date("1950-05-05"));
    }

    @Before
    public void setUp() throws SQLException, ServiceFailureException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds, MissionManager.class.getResource("createTables.sql"));
        manager = new SpyAgencyManagerImpl();
        manager.setDataSource(ds);
        agentManager = new AgentManagerImpl();
        agentManager.setDataSource(ds);
        missionManager = new MissionManagerImpl();
        missionManager.setDataSource(ds);
        prepareTestData();
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds, MissionManager.class.getResource("dropTables.sql"));
    }

    @Test
    public void assignAgentOnMission() throws ServiceFailureException {
        assertNull(manager.findMissionWithAgent(a1));
        assertNull(manager.findMissionWithAgent(a2));
        assertNull(manager.findMissionWithAgent(a3));
        assertNull(manager.findMissionWithAgent(a4));
        assertNull(manager.findMissionWithAgent(a5));

        manager.assignAgentOnMission(a1, m3);
        manager.assignAgentOnMission(a5, m1);
        manager.assignAgentOnMission(a3, m3);

        List<Agent> agentsOnMission1 = Arrays.asList(a5);
        List<Agent> agentsOnMission2 = Collections.emptyList();
        List<Agent> agentsOnMission3 = Arrays.asList(a1, a3);

        assertAgentCollectionDeepEquals(agentsOnMission1, manager.getAgentsOnMission(m1));
        assertAgentCollectionDeepEquals(agentsOnMission2, manager.getAgentsOnMission(m2));
        assertAgentCollectionDeepEquals(agentsOnMission3, manager.getAgentsOnMission(m3));

        assertEquals(m3, manager.findMissionWithAgent(a1));
        assertMissionDeepEquals(m3, manager.findMissionWithAgent(a1));
        assertNull(manager.findMissionWithAgent(a2));
        assertEquals(m3, manager.findMissionWithAgent(a3));
        assertMissionDeepEquals(m3, manager.findMissionWithAgent(a3));
        assertNull(manager.findMissionWithAgent(a4));
        assertEquals(m1, manager.findMissionWithAgent(a5));
        assertMissionDeepEquals(m1, manager.findMissionWithAgent(a5));

        try {
            manager.assignAgentOnMission(a1, m3);
            fail();
        } catch (IllegalEntityException e) {
        }
        try {
            manager.assignAgentOnMission(a1, m2);
            fail();
        } catch (IllegalEntityException e) {
        }
        try {
            manager.assignAgentOnMission(null, m2);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            manager.assignAgentOnMission(agentWithNullId, m2);
            fail();
        } catch (IllegalEntityException e) {
        }
        try {
            manager.assignAgentOnMission(agentNotInDB, m2);
            fail();
        } catch (IllegalEntityException e) {
        }
        try {
            manager.assignAgentOnMission(a2, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            manager.assignAgentOnMission(a2, missionWithNullId);
            fail();
        } catch (IllegalEntityException e) {
        }
        try {
            manager.assignAgentOnMission(a2, missionNotInDB);
            fail();
        } catch (IllegalEntityException e) {
        }
        try {
            manager.assignAgentOnMission(a2, m1);
            fail();
        } catch (IllegalEntityException e) {
        }

        assertAgentCollectionDeepEquals(agentsOnMission1, manager.getAgentsOnMission(m1));
        assertAgentCollectionDeepEquals(agentsOnMission2, manager.getAgentsOnMission(m2));
        assertAgentCollectionDeepEquals(agentsOnMission3, manager.getAgentsOnMission(m3));

        assertEquals(m3, manager.findMissionWithAgent(a1));
        assertNull(manager.findMissionWithAgent(a2));
        assertEquals(m3, manager.findMissionWithAgent(a3));
        assertNull(manager.findMissionWithAgent(a4));
        assertEquals(m1, manager.findMissionWithAgent(a5));
    }

    @Test
    public void findMissionWithAgent() throws ServiceFailureException {
        assertNull(manager.findMissionWithAgent(a1));
        assertNull(manager.findMissionWithAgent(a2));
        assertNull(manager.findMissionWithAgent(a3));
        assertNull(manager.findMissionWithAgent(a4));
        assertNull(manager.findMissionWithAgent(a5));

        manager.assignAgentOnMission(a1, m3);

        assertEquals(m3, manager.findMissionWithAgent(a1));
        assertMissionDeepEquals(m3, manager.findMissionWithAgent(a1));
        assertNull(manager.findMissionWithAgent(a2));
        assertNull(manager.findMissionWithAgent(a3));
        assertNull(manager.findMissionWithAgent(a4));
        assertNull(manager.findMissionWithAgent(a5));

        try {
            manager.findMissionWithAgent(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            manager.findMissionWithAgent(agentWithNullId);
            fail();
        } catch (IllegalEntityException e) {
        }
    }

    @Test
    public void getAgentsOnMission() throws ServiceFailureException {
        assertTrue(manager.getAgentsOnMission(m1).isEmpty());
        assertTrue(manager.getAgentsOnMission(m2).isEmpty());
        assertTrue(manager.getAgentsOnMission(m3).isEmpty());

        manager.assignAgentOnMission(a2, m3);
        manager.assignAgentOnMission(a3, m2);
        manager.assignAgentOnMission(a4, m3);
        manager.assignAgentOnMission(a5, m2);

        List<Agent> agentsOnMission2 = Arrays.asList(a3, a5);
        List<Agent> agentsOnMission3 = Arrays.asList(a2, a4);

        assertTrue(manager.getAgentsOnMission(m1).isEmpty());
        assertAgentCollectionDeepEquals(agentsOnMission2, manager.getAgentsOnMission(m2));
        assertAgentCollectionDeepEquals(agentsOnMission3, manager.getAgentsOnMission(m3));

        try {
            manager.getAgentsOnMission(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            manager.getAgentsOnMission(missionWithNullId);
            fail();
        } catch (IllegalEntityException e) {
        }

    }
    
    @Test
    public void findMissionsWithSomeFreeSpace() throws ServiceFailureException {
        List<Mission> notFullMissions = Arrays.asList(m1,m2,m3);
        assertMissionCollectionDeepEquals(notFullMissions, manager.findMissionsWithSomeFreeSpace());
        
        manager.assignAgentOnMission(a1, m3);
        manager.assignAgentOnMission(a3, m3);
        manager.assignAgentOnMission(a5, m1);
        
        notFullMissions = Arrays.asList(m2, m3);
        assertMissionCollectionDeepEquals(notFullMissions, manager.findMissionsWithSomeFreeSpace());
    }
    
    @Test
    public void findUnassignedAgents() throws ServiceFailureException {
        List <Agent> unassignedAgents = Arrays.asList(a1, a2, a3, a4, a5);
        assertAgentCollectionDeepEquals(unassignedAgents, manager.findUnassignedAgents());
        
        manager.assignAgentOnMission(a1, m3);
        manager.assignAgentOnMission(a2, m3);
        manager.assignAgentOnMission(a5, m1);
        
        unassignedAgents = Arrays.asList(a3, a4);
        assertAgentCollectionDeepEquals(unassignedAgents, manager.findUnassignedAgents());
    }

    @Test
    public void removeAgentFromMission() throws ServiceFailureException {
        manager.assignAgentOnMission(a1, m3);
        manager.assignAgentOnMission(a3, m3);
        manager.assignAgentOnMission(a4, m3);
        manager.assignAgentOnMission(a5, m1);
        
        assertEquals(m3, manager.findMissionWithAgent(a1));
        assertNull(manager.findMissionWithAgent(a2));
        assertEquals(m3, manager.findMissionWithAgent(a3));
        assertEquals(m3, manager.findMissionWithAgent(a4));
        assertEquals(m1, manager.findMissionWithAgent(a5));
        
        manager.removeAgentFromMission(a3, m3);
        
        List<Agent> agentsOnMission1 = Arrays.asList(a5);
        List<Agent> agentsOnMission2 = Collections.emptyList();
        List<Agent> agentsOnMission3 = Arrays.asList(a1,a4);
        
        assertAgentCollectionDeepEquals(agentsOnMission1, manager.getAgentsOnMission(m1));
        assertAgentCollectionDeepEquals(agentsOnMission2, manager.getAgentsOnMission(m2));
        assertAgentCollectionDeepEquals(agentsOnMission3, manager.getAgentsOnMission(m3));
        
        assertEquals(m3, manager.findMissionWithAgent(a1));
        assertNull(manager.findMissionWithAgent(a2));
        assertNull(manager.findMissionWithAgent(a3));
        assertEquals(m3, manager.findMissionWithAgent(a4));
        assertEquals(m1, manager.findMissionWithAgent(a5));
        
        try {
            manager.removeAgentFromMission(a3, m1);
            fail();
        } catch (IllegalEntityException e) {}
        try {
            manager.removeAgentFromMission(a1, m1);
            fail();
        } catch (IllegalEntityException e) {}
        try {
            manager.removeAgentFromMission(null, m2);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            manager.removeAgentFromMission(agentWithNullId, m2);
            fail();
        } catch (IllegalEntityException e) {}
        try {
            manager.removeAgentFromMission(agentNotInDB, m2);
            fail();
        } catch (IllegalEntityException e) {}
        try {
            manager.removeAgentFromMission(a2, null);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            manager.removeAgentFromMission(a2, missionWithNullId);
            fail();
        } catch (IllegalEntityException e) {}
        try {
            manager.removeAgentFromMission(a2, missionNotInDB);
            fail();
        } catch (IllegalEntityException e) {}
        
        assertAgentCollectionDeepEquals(agentsOnMission1, manager.getAgentsOnMission(m1));
        assertAgentCollectionDeepEquals(agentsOnMission2, manager.getAgentsOnMission(m2));
        assertAgentCollectionDeepEquals(agentsOnMission3, manager.getAgentsOnMission(m3));
        
        assertEquals(m3, manager.findMissionWithAgent(a1));
        assertNull(manager.findMissionWithAgent(a2));
        assertNull(manager.findMissionWithAgent(a3));
        assertEquals(m3, manager.findMissionWithAgent(a4));
        assertEquals(m1, manager.findMissionWithAgent(a5));
    }
    
    @Test
    public void tryToAccomplishMission() throws ServiceFailureException {
        
        //test if empty mission has inProgress == false after successful execution
        manager.tryToAccomplishMission(m1, 1);
        assertEquals(missionManager.findMissionById(m1.getId()).getInProgress(), false);
        
        //test if mission with 2 assigned agents has inProgress == false and
        //both agents have their isDead set to false after successfil execution
        manager.assignAgentOnMission(a1, m2);
        manager.assignAgentOnMission(a2, m2);
        manager.tryToAccomplishMission(m2, 1);
        assertEquals(missionManager.findMissionById(m2.getId()).getInProgress(), false);
        assertEquals(agentManager.findAgentById(a1.getId()).isIsDead(), false);
        assertEquals(agentManager.findAgentById(a2.getId()).isIsDead(), false);
        
        //test if mission with 2 assigned agents has inProgress == false and 
        //both agents have their isDead set to true after failed execution
        manager.assignAgentOnMission(a3, m3);
        manager.assignAgentOnMission(a4, m3);
        manager.tryToAccomplishMission(m3, 0);
        assertEquals(missionManager.findMissionById(m3.getId()).getInProgress(), false);
        assertEquals(agentManager.findAgentById(a3.getId()).isIsDead(), true);
        assertEquals(agentManager.findAgentById(a4.getId()).isIsDead(), true);
    }
}
