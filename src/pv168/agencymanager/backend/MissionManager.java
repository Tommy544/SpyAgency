package pv168.agencymanager.backend;

import java.util.List;
import pv168.common.ServiceFailureException;

public interface MissionManager {
    void createMission(Mission mission) throws ServiceFailureException;
    
    Mission findMissionById(Long id) throws ServiceFailureException;
    
    List<Mission> getAllMissions() throws ServiceFailureException;
    
    void removeMission(Mission mission) throws ServiceFailureException;
    
    void updateMission(Mission mission) throws ServiceFailureException;
}
