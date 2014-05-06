/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pv168.agencymanager.swing;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import pv168.agencymanager.backend.Agent;

/**
 *
 * @author vlado
 */
public class AgentsTableModel extends AbstractTableModel{
    
    private List<Agent> agents = new ArrayList<>();
    private ResourceBundle strings;
    
    public AgentsTableModel (ResourceBundle strings) {
        this.strings = strings;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: 
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return Date.class;
            case 3:
                return Boolean.class;
            default:
                throw new IllegalArgumentException(strings.getString("columnIndex_OOB"));
        }
    }

    @Override
    public int getRowCount() {
        return agents.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         if (rowIndex > agents.size()) {
            throw new IllegalArgumentException(strings.getString("rowIndex_OOB"));
         }
         
         Agent agent = agents.get(rowIndex);
         switch (columnIndex) {
             case 0: 
                 return agent.getName();
             case 1:
                 return agent.getAgentNumber();
             case 2:
                 return agent.getDateOfEnrollment();
             case 3:
                 return agent.isIsDead();
             default:
                 throw new IllegalArgumentException(strings.getString("columnIndex_OOB"));
         }
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Agent agent = null;
        if (rowIndex > agents.size()) {
            throw new IllegalArgumentException(strings.getString("rowIndex_OOB"));
        }
        else
            agent = agents.get(rowIndex);
        switch (columnIndex) {
            case 0:
            {
                agent.setName((String) aValue);
                break;
            }
            case 1:
            {
                agent.setAgentNumber((Integer) aValue);
                break;
            }
            case 2:
            {
                agent.setDateOfEnrollment((Date) aValue);
                break;
            }
            case 3:
            {
                agent.setIsDead((Boolean) aValue);
                break;
            }
            default:
                throw new IllegalArgumentException(strings.getString("columnIndex_OOB"));
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return strings.getString("name");
            case 1:
                return strings.getString("agentNumber");
            case 2:
                return strings.getString("dateOfEnrollment");
            case 3:
                return strings.getString("isDead");
            default:
                throw new IllegalArgumentException(strings.getString("columnIndex_OOB"));
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return ((columnIndex >= 0) && (columnIndex <= 3));
    }
    
    public void add(Agent agent)
    {
        agents.add(agent);
        fireTableRowsInserted((agents.size() - 1), agents.size() - 1);
    }
}
