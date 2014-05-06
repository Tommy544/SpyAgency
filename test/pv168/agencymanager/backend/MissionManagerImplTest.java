package pv168.agencymanager.backend;

import pv168.common.ValidationException;
import pv168.common.IllegalEntityException;
import pv168.common.DBUtils;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.dbcp.BasicDataSource;

import static org.junit.Assert.*;
import pv168.common.ServiceFailureException;


public class MissionManagerImplTest {
    
    private MissionManagerImpl manager;
    private DataSource ds;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        //we will use in memory database
        ds.setUrl("jdbc:derby:memory:missionmanager-test;create=true");
        return ds;
    }
    
    private Date date(String date) {
        return Date.valueOf(date);
    }    
    
    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,MissionManager.class.getResource("createTables.sql"));
        manager = new MissionManagerImpl();
        manager.setDataSource(ds);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,MissionManager.class.getResource("dropTables.sql"));
    }
 

    @Test
    public void createMission() throws ServiceFailureException {
        Mission mission = newMission("vesmir", date("1960-01-01"), false, 18, "Kill all");
        manager.createMission(mission);

        Long missionId = mission.getId();
        assertNotNull(missionId);
        Mission result = manager.findMissionById(missionId);
        assertEquals(mission, result);
        assertNotSame(mission, result);
        assertDeepEquals(mission, result);
    }
    
    @Test
    public void findMissionById() throws ServiceFailureException {
        
        assertNull(manager.findMissionById(1l));
        
        Mission mission = newMission("vesmir", date("1960-01-01"), false, 18, "Kill all");
        manager.createMission(mission);
        Long missionId = mission.getId();

        Mission result = manager.findMissionById(missionId);
        assertEquals(mission, result);
        assertDeepEquals(mission, result);
    }

    @Test
    public void getAllMissions() throws ServiceFailureException {

        assertTrue(manager.getAllMissions().isEmpty());

        Mission m1 = newMission("vesmir", date("1960-01-01"), false, 18, "Kill all");
        Mission m2 = newMission("diera", date("1960-01-01"), true, 88, "lalala");

        manager.createMission(m1);
        manager.createMission(m2);

        List<Mission> expected = Arrays.asList(m1,m2);
        List<Mission> actual = manager.getAllMissions();

        Collections.sort(actual,idComparator);
        Collections.sort(expected,idComparator);

        //assertEquals(expected, actual);
        assertMissionCollectionDeepEquals(expected, actual);
    }

    @Test
    public void deleteMission() throws ServiceFailureException {

        Mission m1 = newMission("vesmir", date("1960-01-01"), false, 18, "Kill all");
        Mission m2 = newMission("diera", date("1960-01-01"), true, 88, "lalala");
        manager.createMission(m1);
        manager.createMission(m2);
        
        assertNotNull(manager.findMissionById(m1.getId()));
        assertNotNull(manager.findMissionById(m2.getId()));

        manager.removeMission(m1);
        
        assertNull(manager.findMissionById(m1.getId()));
        assertNotNull(manager.findMissionById(m2.getId()));
                
    }

    @Test
    public void deleteMissionWithWrongAttributes() throws ServiceFailureException {

        Mission mission = newMission("vesmir", date("1960-01-01"), false, 18, "Kill all");
        
        try {
            manager.removeMission(null);
            fail("null argument");
        } catch (IllegalArgumentException ex) {
        }

        try {
            mission.setId(null);
            manager.removeMission(mission);
            fail("negative argument");
        } catch (IllegalEntityException ex) {
        }

        try {
            mission.setId(1l);
            manager.removeMission(mission);
            fail("illegal argument");
        } catch (IllegalEntityException ex) {
        }        

    }
    
    static Mission newMission(String codeName, Date dateCreated, boolean inProgress, int maxNumberOfAgents, String notes) {
        Mission mission = new Mission();
        mission.setCodeName(codeName);
        mission.setDateCreated(dateCreated);
        mission.setInProgress(inProgress);
        mission.setMaxNumberOfAgents(maxNumberOfAgents);
        mission.setNotes(notes);
        return mission;
    }

    static void assertDeepEquals(Mission expected, Mission actual) {
        assertEquals(expected.getCodeName(), actual.getCodeName());
        assertEquals(expected.getDateCreated(), actual.getDateCreated());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getInProgress(), actual.getInProgress());
        assertEquals(expected.getMaxNumberOfAgents(), actual.getMaxNumberOfAgents());
        assertEquals(expected.getNotes(), actual.getNotes());
    }

    
    static void assertMissionDeepEquals(Mission expected, Mission actual) {
        assertDeepEquals(expected, actual);
    }
    
    private static Comparator<Mission> idComparator = new Comparator<Mission>() {

        @Override
        public int compare(Mission o1, Mission o2) {
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
    
    static void assertMissionCollectionDeepEquals(List<Mission> expected, List<Mission> actual) {
        
        assertEquals(expected.size(), actual.size());
        List<Mission> expectedSortedList = new ArrayList<Mission>(expected);
        List<Mission> actualSortedList = new ArrayList<Mission>(actual);
        Collections.sort(expectedSortedList,idComparator);
        Collections.sort(actualSortedList,idComparator);
        for (int i = 0; i < expectedSortedList.size(); i++) {
            assertDeepEquals(expectedSortedList.get(i), actualSortedList.get(i));
        }
        
        
    }

}
