/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pv168.agencymanager.backend;

import java.util.List;
import pv168.agencymanager.backend.Mission;
import pv168.common.ServiceFailureException;

/**
 * This service allows associations between agents and missions.
 *
 * @author vlado
 */
public interface SpyAgencyManager {
    
    /**
     * Method assigns agent on a specific mission.
     * 
     * @param agent Agent to be assigned
     * @param mission Mission on which Agent should be assigned
     * @throws pv168.common.ServiceFailureException
     */
    void assignAgentOnMission(Agent agent, Mission mission) throws ServiceFailureException;
    
    /**
     * Method finds and returns Mission on which specified agent is currently
     * assigned.
     * 
     * @param agent Agent that is used to find Mission.
     * @return Mission on which specified Agent is assigned.
     * @throws pv168.common.ServiceFailureException
     */
    Mission findMissionWithAgent(Agent agent) throws ServiceFailureException;
    
    /**
     * Method finds and returns a list of agents that are currently assigned
     * on a specific Mission.
     * 
     * @param mission A Mission from which we want to get a list of Agents.
     * @return A list of Agents that are currently assigned on a specified
     *      Mission.
     * @throws pv168.common.ServiceFailureException
     */
    List<Agent> getAgentsOnMission(Mission mission) throws ServiceFailureException;
    
    /**
     * Method returns a list of missions that have at least one free space for 
     * agent assignment.
     * 
     * @return A list of missions that have some free space.
     * @throws pv168.common.ServiceFailureException
     */
    List<Mission> findMissionsWithSomeFreeSpace() throws ServiceFailureException;
    
    /**
     * Method returns a list of all agents that have not yet been assigned for a mission
     * 
     * @return a list of unassigned agents
     * @throws ServiceFailureException 
     */
    List<Agent> findUnassignedAgents() throws ServiceFailureException;
    
    /**
     * Method tries to remove assignment between specified Agent and specified
     * Mission.
     * 
     * @param agent Agent to be removed from assignment.
     * @param mission Mission from which Agent should be removed.
     * @throws pv168.common.ServiceFailureException
     */
    void removeAgentFromMission(Agent agent, Mission mission) throws ServiceFailureException;
    
    /**
     * Simple method to simulate execution of a Mission.
     * If mission does succeed, mission parameter inProgress will be set to false,
     * If mission does not succeed, mission parameter will be set to false and parameter isDead
     * for all agents that were assigned to this mission will be set to true;
     * 
     * @param mission A Mission to be executed.
     * @param random if set as 0, mission will fail, if set to 1, mission will succeed,
     *      otherwise the function will generate a random number (0 or 1).
     * @throws pv168.common.ServiceFailureException
     */
    void tryToAccomplishMission(Mission mission, int random) throws ServiceFailureException;
}
