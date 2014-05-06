/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pv168.agencymanager.backend;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import pv168.common.DBUtils;
import pv168.common.IllegalEntityException;
import pv168.common.ServiceFailureException;
import pv168.common.ValidationException;

/**
 *
 * @author vlado
 */
public class AgentManagerImplTest {
    
    private AgentManagerImpl manager;
    private DataSource ds;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        //we will use in memory database
        ds.setUrl("jdbc:derby:memory:agentmanager-test;create=true");
        return ds;
    }
    
    @Before
     public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,AgentManager.class.getResource("createTables.sql"));
        manager = new AgentManagerImpl();
        manager.setDataSource(ds);
    
        
//        File file = new File("sqlQuerry.sql");
//        FileInputStream fis = new FileInputStream(file);
//        PreparedStatement ps = connection.prepareStatement("?");
//        ps.setAsciiStream(1, fis);
//        ps.execute();
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,AgentManager.class.getResource("dropTables.sql"));    
    }
    
    private Date date(String date) {
        return Date.valueOf(date);
    }    
    
    @Test
    public void trainAgent() throws ServiceFailureException {
        Agent agent = new Agent(007, "James Bond", false, date("1980-01-01"));
        manager.trainAgent(agent);
        
        Long agentId = agent.getId();
        assertNotNull(agentId);
        Agent result = manager.findAgentById(agentId);
        assertEquals(agent, result);
        assertNotSame(agent, result);
        assertDeepEquals(agent, result);
    }
    
    @Test
    public void trainAgentWithWrongAttributes() throws ServiceFailureException {

        try {
            manager.trainAgent(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        Agent agent = new Agent(007, "James Bond", false, date("1980-01-01"));
        agent.setId(1l);
        try {
            manager.trainAgent(agent);
            fail();
        } catch (IllegalEntityException ex) {
            //OK
        }
        
        agent = new Agent(-100, "James Bond", false, date("1980-01-01"));
        try {
            manager.trainAgent(agent);
            fail();
        } catch (ValidationException ex) {
            //OK
        }

        agent = new Agent(007, null, false, date("1980-01-01"));
        try {
            manager.trainAgent(agent);
            fail();
        } catch (ValidationException ex) {
            //OK
        }

        agent = new Agent(007, "James Bond", false, null);
        try {
            manager.trainAgent(agent);
            fail();
        } catch (ValidationException ex) {
            //OK
        }

        // these variants should be ok
        agent = new Agent(007, "James Bond", false, date("1980-01-01"));
        manager.trainAgent(agent);
        Agent result = manager.findAgentById(agent.getId()); 
        assertNotNull(result);

        agent = new Agent(111, "Janko Hrasko", true, date("1955-06-10"));
        manager.trainAgent(agent);
        result = manager.findAgentById(agent.getId()); 
        assertNotNull(result);
    }
    
    @Test
    public void getAgent() throws ServiceFailureException {
        
        assertNull(manager.findAgentById(1l));
        
        Agent agent = new Agent(007, "James Bond", false, date("1980-01-01"));
        manager.trainAgent(agent);
        Long agentId = agent.getId();

        Agent result = manager.findAgentById(agentId);
        assertEquals(agent, result);
        assertDeepEquals(agent, result);
    }
    
    @Test
    public void updateAgent() throws ServiceFailureException {
        Agent a1 = new Agent(007, "James Bond", false, date("1980-01-01"));
        Agent a2 = new Agent(111, "Janko Hrasko", true, date("1955-06-10"));
        manager.trainAgent(a1);
        manager.trainAgent(a2);
        Long a1Id = a1.getId();

        a1 = manager.findAgentById(a1Id);
        a1.setAgentNumber(666);
        manager.updateAgent(a1);        
        assertEquals(666, a1.getAgentNumber());
        assertEquals("James Bond", a1.getName());
        assertFalse(a1.isIsDead());
        assertEquals(date("1980-01-01"), a1.getDateOfEnrollment());
        
        a1Id = a1.getId();
        a1 = manager.findAgentById(a1Id);
        a1.setName("Super Agent");
        manager.updateAgent(a1);        
        assertEquals(666, a1.getAgentNumber());
        assertEquals("Super Agent", a1.getName());
        assertFalse(a1.isIsDead());
        assertEquals(date("1980-01-01"), a1.getDateOfEnrollment());
        
        a1Id = a1.getId();
        a1 = manager.findAgentById(a1Id);
        a1.setIsDead(true);
        manager.updateAgent(a1);        
        assertEquals(666, a1.getAgentNumber());
        assertEquals("Super Agent", a1.getName());
        assertTrue(a1.isIsDead());
        assertEquals(date("1980-01-01"), a1.getDateOfEnrollment());
        
        a1Id = a1.getId();
        a1 = manager.findAgentById(a1Id);
        a1.setDateOfEnrollment(date("2000-12-29"));
        manager.updateAgent(a1);        
        assertEquals(666, a1.getAgentNumber());
        assertEquals("Super Agent", a1.getName());
        assertTrue(a1.isIsDead());
        assertEquals(date("2000-12-29"), a1.getDateOfEnrollment());

        // Check if updates didn't affected other records
        assertDeepEquals(a2, manager.findAgentById(a2.getId()));
    }
    
    @Test
    public void updateAgentWithWrongAttributes() throws ServiceFailureException {

        Agent agent = new Agent(007, "James Bond", false, date("1980-01-01"));
        manager.trainAgent(agent);
        Long agentId = agent.getId();
        
        try {
            manager.updateAgent(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
        
        try {
            agent = manager.findAgentById(agentId);
            agent.setId(null);
            manager.updateAgent(agent);        
            fail();
        } catch (IllegalEntityException ex) {
            //OK
        }
        
        try {
            agent = manager.findAgentById(agentId);
            agent.setId(agentId -1);
            manager.updateAgent(agent);        
            fail();
        } catch (IllegalEntityException ex) {
            //OK
        }
        
        try {
            agent = manager.findAgentById(agentId);
            agent.setName(null);
            manager.updateAgent(agent);        
            fail();
        } catch (ValidationException ex) {
            //OK
        }
        
        try {
            agent = manager.findAgentById(agentId);
            agent.setName("");
            manager.updateAgent(agent);        
            fail();
        } catch (ValidationException ex) {
            //OK
        }
    }
    
    @Test
    public void dismissAgent() throws ServiceFailureException {

        Agent a1 = new Agent(007, "James Bond", false, date("1980-01-01"));
        Agent a2 = new Agent(111, "Janko Hrasko", true, date("1955-06-10"));
        manager.trainAgent(a1);
        manager.trainAgent(a2);
        
        assertNotNull(manager.findAgentById(a1.getId()));
        assertNotNull(manager.findAgentById(a2.getId()));

        manager.dismissAgent(a1);
        
        assertNull(manager.findAgentById(a1.getId()));
        assertNotNull(manager.findAgentById(a2.getId()));                
    }
    
    @Test
    public void dismissAgentWithWrongAttributes() throws ServiceFailureException {

        Agent agent = new Agent(007, "James Bond", false, date("1980-01-01"));
        
        try {
            manager.dismissAgent(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            agent.setId(null);
            manager.dismissAgent(agent);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            agent.setId(1l);
            manager.dismissAgent(agent);
            fail();
        } catch (IllegalEntityException ex) {
            //OK
        }        
    }
    
    @Test
    public void getAllAgents() throws ServiceFailureException {

        assertTrue(manager.getAllAgents().isEmpty());

        Agent a1 = new Agent(007, "James Bond", false, date("1980-01-01"));
        Agent a2 = new Agent(111, "Janko Hrasko", true, date("1955-06-10"));

        manager.trainAgent(a1);
        manager.trainAgent(a2);

        List<Agent> expected = Arrays.asList(a1,a2);
        List<Agent> actual = manager.getAllAgents();

        Collections.sort(actual,idComparator);
        Collections.sort(expected,idComparator);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }
    
    private void assertDeepEquals(List<Agent> expectedList, List<Agent> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Agent expected = expectedList.get(i);
            Agent actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }
    
    static void assertDeepEquals(Agent expected, Agent actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAgentNumber(), actual.getAgentNumber());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.isIsDead(), actual.isIsDead());
        assertEquals(expected.getDateOfEnrollment(), actual.getDateOfEnrollment());
    }
    
    static void assertAgentCollectionDeepEquals(List<Agent> expected, List<Agent> actual) {
        assertEquals(expected.size(), actual.size());
        List<Agent> expectedSortedList = new ArrayList<>(expected);
        List<Agent> actualSortedList = new ArrayList<Agent>(actual);
        Collections.sort(expectedSortedList, agentKeyComparator);
        Collections.sort(actualSortedList, agentKeyComparator);
        for (int i = 0; i < expectedSortedList.size(); i++) {
            assertDeepEquals(expectedSortedList.get(i), actualSortedList.get(i));
        }
    }

    private static Comparator<Agent> idComparator = new Comparator<Agent>() {

        @Override
        public int compare(Agent o1, Agent o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
    };
    
    private static Comparator<Agent> agentKeyComparator = new Comparator<Agent>() {
        @Override
        public int compare(Agent o1, Agent o2) {
            Long k1 = o1.getId();
            Long k2 = o2.getId();
            if (k1 == null && k2 == null) {
                return 0;
            } else if (k1 == null && k2 != null) {
                return -1;
            } else if (k1 != null && k2 == null) {
                return 1;
            } else {
                return k1.compareTo(k2);
            }
        }
    };
}
