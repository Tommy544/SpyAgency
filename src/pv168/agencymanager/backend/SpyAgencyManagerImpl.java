/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv168.agencymanager.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import pv168.common.DBUtils;
import pv168.common.IllegalEntityException;
import pv168.common.ServiceFailureException;

/**
 *
 * @author vlado
 */
public class SpyAgencyManagerImpl implements SpyAgencyManager {

    private static final Logger logger = Logger.getLogger(
            SpyAgencyManagerImpl.class.getName());

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is null");
        }
    }

    private static void checkIfMissionHasSpace(Connection conn, Mission mission)
            throws IllegalEntityException, SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "SELECT maxNumberOfAgents, COUNT(agent.id) as agentsCount "
                    + "FROM mission LEFT JOIN agent ON mission.id = agent.missionId "
                    + "WHERE mission.id = ? "
                    + "GROUP BY mission.id, maxNumberOfAgents");
            st.setLong(1, mission.getId());
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    if (rs.getInt("maxNumberOfAgents") <= rs.getInt("agentsCount")) {
                        throw new IllegalEntityException("Mission " + mission + " is already full");
                    }
                } else {
                    throw new IllegalEntityException("Mission " + mission + " does not exist in DB");
                }
            }
        } finally {
            DBUtils.closeQuietly(null, st);
        }
    }

    @Override
    public void assignAgentOnMission(Agent agent, Mission mission) throws ServiceFailureException {
        checkDataSource();
        if (agent == null) {
            throw new IllegalArgumentException("Agent is null when trying to assing on a Mission");
        }
        if (agent.getId() == null) {
            throw new IllegalEntityException("Agent id is null when trying to assign on a Mission");
        }
        if (mission == null) {
            throw new IllegalArgumentException("Mission is null when assigning agent");
        }
        if (mission.getId() == null) {
            throw new IllegalEntityException("Mission id is null when assigning agent");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            checkIfMissionHasSpace(conn, mission);

            st = conn.prepareStatement(
                    "UPDATE agent SET missionId = ? WHERE id = ? AND missionId IS NULL");
            st.setLong(1, mission.getId());
            st.setLong(2, agent.getId());
            int count = st.executeUpdate();
            if (count == 0) {
                throw new IllegalEntityException("Agent " + agent + " is not found or is already assigned");
            }
            DBUtils.checkUpdatesCount(count, agent, false);
            conn.commit();
        } catch (SQLException e) {
            String msg = "Error when assigning agent on a mission";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public Mission findMissionWithAgent(Agent agent) throws ServiceFailureException, IllegalEntityException {
        checkDataSource();
        if (agent == null) {
            throw new IllegalArgumentException("Agent is null when trying to assing on a Mission");
        }
        if (agent.getId() == null) {
            throw new IllegalEntityException("Agent id is null when trying to assign on a Mission");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT mission.id, codeName, dateCreated, inProgress, maxNumberOfAgents, notes "
                    + "FROM mission JOIN agent ON mission.id = agent.missionId "
                    + "WHERE agent.id = ?");
            st.setLong(1, agent.getId());
            return MissionManagerImpl.executeQueryForSingleMission(st);
        } catch (SQLException e) {
            String msg = "Error when trying to find Mission with Agent " + agent;
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }

    }

    @Override
    public List<Agent> getAgentsOnMission(Mission mission) throws ServiceFailureException, IllegalEntityException {
        checkDataSource();
        if (mission == null) {
            throw new IllegalArgumentException("Mission is null when assigning agent");
        }
        if (mission.getId() == null) {
            throw new IllegalEntityException("Mission id is null when assigning agent");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT agent.id, number, name, isDead, enrollment "
                    + "FROM agent JOIN mission ON mission.id = agent.missionId "
                    + "WHERE mission.id = ?");
            st.setLong(1, mission.getId());
            return AgentManagerImpl.executeQueryForMultipleAgents(st);
        } catch (SQLException e) {
            String msg = "Error when trying to find agents on a mission" + mission;
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public List<Mission> findMissionsWithSomeFreeSpace() throws ServiceFailureException {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT mission.id, codeName, dateCreated, inProgress, maxNumberOfAgents, notes "
                    + "FROM mission LEFT JOIN agent ON mission.id = agent.missionId "
                    + "GROUP BY mission.id, codeName, dateCreated, inProgress, maxNumberOfAgents, notes "
                    + "HAVING count(agent.id) < maxNumberOfAgents");
            return MissionManagerImpl.executeQueryForMultipleMissions(st);
        } catch (SQLException e) {
            String msg = "Error when trying to find missions with free space.";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }

    }

    @Override
    public List<Agent> findUnassignedAgents() throws ServiceFailureException {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id, name, number, isDead, enrollment FROM "
                    + "agent WHERE missionId IS NULL");
            return AgentManagerImpl.executeQueryForMultipleAgents(st);
        } catch (SQLException e) {
            String msg = "Error when trying to find unassigned agents";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void removeAgentFromMission(Agent agent, Mission mission) throws ServiceFailureException, IllegalEntityException {
        checkDataSource();
        if (agent == null) {
            throw new IllegalArgumentException("Agent is null when trying to assing on a Mission");
        }
        if (agent.getId() == null) {
            throw new IllegalEntityException("Agent id is null when trying to assign on a Mission");
        }
        if (mission == null) {
            throw new IllegalArgumentException("Mission is null when assigning agent");
        }
        if (mission.getId() == null) {
            throw new IllegalEntityException("Mission id is null when assigning agent");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE agent SET missionId = NULL WHERE id = ? AND missionId = ?");
            st.setLong(1, agent.getId());
            st.setLong(2, mission.getId());
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, false);
            conn.commit();
        } catch (SQLException e) {
            String msg = "Error when removing agent from mission";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

    }

    @Override
    public void tryToAccomplishMission(Mission mission, int random) throws ServiceFailureException {
        checkDataSource();
        if (mission == null) {
            throw new IllegalArgumentException("Mission is null when assigning agent");
        }
        if (mission.getId() == null) {
            throw new IllegalEntityException("Mission id is null when assigning agent");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE mission SET inProgress = false WHERE id = ?");
            st.setLong(1, mission.getId());
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, mission, false);

            int successful;
            if (random == 0 || random == 1) {
                successful = random;
            } else {
                Random rand = new Random();
                successful = rand.nextInt(2);
            }
            if (successful == 0) {
                st = conn.prepareStatement(
                        "SELECT agent.id as identification FROM mission LEFT JOIN agent ON "
                        + "mission.id = agent.missionId WHERE mission.id = ?");
                st.setLong(1, mission.getId());
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        Long id = rs.getLong("identification");
                        st = conn.prepareStatement(
                                "UPDATE agent SET isDead = true WHERE id = ?");
                        st.setLong(1, id);
                        count = st.executeUpdate();
                        if (count > 1) {
                            throw new ServiceFailureException("Method tryToAccomplishMission is trying to update more rows");
                        }
                    }
                }
            }
            st = conn.prepareStatement("UPDATE mission SET inProgress = false WHERE id =  ?");
            st.setLong(1, mission.getId());
            count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, mission, false);

            conn.commit();
        } catch (SQLException e) {
            String msg = "Error when trying to accomplish mission";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

    }

}
