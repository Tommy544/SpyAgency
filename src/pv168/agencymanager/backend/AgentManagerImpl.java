/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv168.agencymanager.backend;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import pv168.common.DBUtils;
import pv168.common.IllegalEntityException;
import pv168.common.ServiceFailureException;
import pv168.common.ValidationException;

/**
 *
 * @author vlado
 */
public class AgentManagerImpl implements AgentManager {

    public static final Logger logger = Logger.getLogger(AgentManagerImpl.class.getName());
//    public AgentManagerImpl(Connection connection) {
//        this.connection = connection;
//    }
//    private Connection connection;

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() throws ServiceFailureException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    @Override
    public void trainAgent(Agent agent) throws ServiceFailureException {
        checkDataSource();
        validate(agent);
        if (agent.getId() != null) {
            throw new IllegalEntityException("agent id is already set when training new agent");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO agent (number, name, isdead, enrollment) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, agent.getAgentNumber());
            st.setString(2, agent.getName());
            st.setBoolean(3, agent.isIsDead());
            st.setDate(4, (Date) agent.getDateOfEnrollment());
//            int addedRows = st.executeUpdate();
//            if (addedRows != 1) {
//                throw new ServiceFailureException("Tried to insert more than one row");
//            }
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            agent.setId(id);
            conn.commit();

//            try (ResultSet keyRS = st.getGeneratedKeys()) {
//                agent.setId(getKey(keyRS, agent));
//            }
        } catch (SQLException e) {
            String msg = "Error when inserting agent ";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }

    }

    @Override
    public void updateAgent(Agent agent) throws ServiceFailureException {
        checkDataSource();
        validate(agent);
        if (agent.getId() == null) {
            throw new IllegalEntityException("agent id is null");
        }

//        Agent isAgent = findAgentById(agent.getId());
//        if (isAgent == null) {
//            throw new IllegalArgumentException("cannot find agent by provided id");
//        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE agent SET number = ?, name = ?, isdead = ?, enrollment = ? WHERE id = ?");
            st.setInt(1, agent.getAgentNumber());
            st.setString(2, agent.getName());
            st.setBoolean(3, agent.isIsDead());
            st.setDate(4, (Date) agent.getDateOfEnrollment());
            st.setLong(5, agent.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, false);
            conn.commit();
        } catch (SQLException e) {
            String msg = "Error when updating agent in the db";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void dismissAgent(Agent agent) throws ServiceFailureException {
        checkDataSource();

        if (agent == null) {
            throw new IllegalArgumentException("null pointer when trying to delete agent");
        }
        if (agent.getId() == null) {
            throw new IllegalArgumentException("trying to delete agent, but recieved null id");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM agent WHERE id = ?");
            st.setLong(1, agent.getId());
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, agent, false);
            conn.commit();
        } catch (SQLException e) {
            String msg = "Error when deleting agent from the db";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public Agent findAgentById(Long id) throws ServiceFailureException {
        checkDataSource();

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id, number, name, isdead, enrollment FROM agent WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSingleAgent(st);
        } catch (SQLException e) {
            String msg = "Error when getting agent with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public Agent findAgentByAgentNumber(Integer number) throws ServiceFailureException {
        checkDataSource();

        if (number == null) {
            throw new IllegalArgumentException("number is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id, number, name, isdead, enrollment FROM agent WHERE number = ?");
            st.setInt(1, number);
            return executeQueryForSingleAgent(st);
        } catch (SQLException e) {
            String msg = "Error when getting agent with number = " + number + " from DB";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    @Override
    public List<Agent> getAllAgents() throws ServiceFailureException {
        checkDataSource();

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id, number, name, isdead, enrollment FROM agent");
            try (ResultSet rs = st.executeQuery()) {
                List<Agent> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(ResultSetToAgent(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            String msg = "Error when getting all agents from DB";
            logger.log(Level.SEVERE, msg, e);
            throw new ServiceFailureException(msg, e);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    static private void validate(Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
//        if (agent.getId() != null) {
//            throw new IllegalArgumentException("agent id is already set");
//        }
        if (agent.getName() == null || agent.getName().equals("")) {
            throw new ValidationException("agent name is null");
        }
        if (agent.getAgentNumber() < 0) {
            throw new ValidationException("agent number is < 0");
        }
        if (agent.getDateOfEnrollment() == null) {
            throw new ValidationException("agent date is null");
        }
    }

    private static Agent ResultSetToAgent(ResultSet rs) throws SQLException {
        Agent agent = new Agent();
        agent.setId(rs.getLong("id"));
        agent.setAgentNumber(rs.getInt("number"));
        agent.setName(rs.getString("name"));
        agent.setIsDead(rs.getBoolean("isdead"));
        agent.setDateOfEnrollment(rs.getDate("enrollment"));
        return agent;
    }

    static Agent executeQueryForSingleAgent(PreparedStatement st) throws SQLException, ServiceFailureException {
        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                Agent agent = ResultSetToAgent(rs);
                if (rs.next()) {
                    throw new ServiceFailureException("Internal error: More entities with the same id found ");
                }
                return agent;
            } else {
                return null;
            }
        }
    }

    static List<Agent> executeQueryForMultipleAgents(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            List<Agent> result = new ArrayList<>();
            while (rs.next()) {
                result.add(ResultSetToAgent(rs));
            }
            return result;
        }
    }

}
