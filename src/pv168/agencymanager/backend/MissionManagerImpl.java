package pv168.agencymanager.backend;

import pv168.common.ServiceFailureException;
import pv168.common.ValidationException;
import pv168.common.IllegalEntityException;
import pv168.common.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class MissionManagerImpl implements MissionManager {

    private static final Logger logger = Logger.getLogger(
            MissionManagerImpl.class.getName());

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    @Override
    public void createMission(Mission mission) throws ServiceFailureException {
        checkDataSource();
        validate(mission);
        if (mission.getId() != null) {
            throw new IllegalEntityException("mission id is already set");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO Mission (codeName,dateCreated,inProgress,maxNumberOfAgents,notes) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, mission.getCodeName());
            st.setDate(2, mission.getDateCreated());
            st.setBoolean(3, mission.getInProgress());
            st.setInt(4, mission.getMaxNumberOfAgents());
            st.setString(5, mission.getNotes());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, mission, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            mission.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting missione into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public Mission findMissionById(Long id) throws ServiceFailureException {
        checkDataSource();

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,codeName,dateCreated,inProgress,maxNumberOfAgents,notes FROM Mission WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSingleMission(st);
        } catch (SQLException ex) {
            String msg = "Error when getting mission with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public List<Mission> getAllMissions() throws ServiceFailureException {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,codeName,dateCreated,inProgress,maxNumberOfAgents,notes FROM Mission");
            return executeQueryForMultipleMissions(st);
        } catch (SQLException ex) {
            String msg = "Error when getting all missions from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void removeMission(Mission mission) throws ServiceFailureException {
        checkDataSource();
        if (mission == null) {
            throw new IllegalArgumentException("mission is null");
        }
        if (mission.getId() == null) {
            throw new IllegalEntityException("mission id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM Mission WHERE id = ?");
            st.setLong(1, mission.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, mission, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting mission from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    @Override
    public void updateMission(Mission mission) throws ServiceFailureException {
        checkDataSource();
        validate(mission);
        if (mission.getId() == null) {
            throw new IllegalEntityException("mission id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE Grave SET codeName = ?, dateCreated = ?, inProgress = ?, maxNumberOfAgents = ?, notes = ? WHERE id = ?");
            st.setString(1, mission.getCodeName());
            st.setDate(2, mission.getDateCreated());
            st.setBoolean(3, mission.getInProgress());
            st.setInt(4, mission.getMaxNumberOfAgents());
            st.setString(5, mission.getNotes());
            st.setLong(6, mission.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, mission, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating mission in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    static Mission executeQueryForSingleMission(PreparedStatement st) throws SQLException, ServiceFailureException {
        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                Mission result = resultMission(rs);
                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal integrity error: more missions with the same id found!");
                }
                return result;
            } else {
                return null;
            }
        }
    }

    static List<Mission> executeQueryForMultipleMissions(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            List<Mission> result = new ArrayList<Mission>();
            while (rs.next()) {
                result.add(resultMission(rs));
            }
            return result;
        }
    }

    private static Mission resultMission(ResultSet rs) throws SQLException {
        Mission result = new Mission();
        result.setId(rs.getLong("id"));
        result.setCodeName(rs.getString("codeName"));
        result.setDateCreated(rs.getDate("dateCreated"));
        result.setInProgress(rs.getBoolean("inProgress"));
        result.setMaxNumberOfAgents(rs.getInt("maxNumberOfAgents"));
        result.setNotes(rs.getString("notes"));
        return result;
    }

    private static void validate(Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("mission is null");
        }
        if (mission.getCodeName() == null) {
            throw new ValidationException("code name is null");
        }
        if (mission.getDateCreated() == null) {
            throw new ValidationException("date is null");
        }
        if (mission.getMaxNumberOfAgents() < 0) {
            throw new ValidationException("max number of agents is not positive number");
        }
    }

}
