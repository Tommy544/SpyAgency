/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pv168.agencymanager.backend;

import pv168.common.ServiceFailureException;
import java.util.List;

/**
 *
 * @author vlado
 */
public interface AgentManager {
    
    /**
     * Method creates a new Agent
     * 
     * @param agent agent to be created
     * @throws pv168.common.ServiceFailureException
     */
    public void trainAgent(Agent agent) throws ServiceFailureException;
    
    /**
     * Method updates agent that is passed as parameter
     * 
     * @param agent agent to be updated
     * @throws pv168.common.ServiceFailureException
     */
    public void updateAgent(Agent agent) throws ServiceFailureException;
    
    /**
     * Method deletes an agent that is passed as parameter
     * 
     * @param agent to be deleted
     * @throws pv168.common.ServiceFailureException
     */
    public void dismissAgent(Agent agent) throws ServiceFailureException;
    
    /**
     * Method finds and returns agent whose id matches parameter id
     * If no agent is found, returns NULL
     * 
     * @param id id of agent to be found
     * @return Agent with specified id
     * @throws pv168.common.ServiceFailureException
     */
    public Agent findAgentById(Long id) throws ServiceFailureException;
    
    /**
     * Method returns a list of all Agents in database
     * 
     * @return List of all agents
     * @throws pv168.common.ServiceFailureException
     */
    public List<Agent> getAllAgents() throws ServiceFailureException;
}
