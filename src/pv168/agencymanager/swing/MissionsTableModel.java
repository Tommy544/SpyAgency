package pv168.agencymanager.swing;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import pv168.agencymanager.backend.Agent;
import pv168.agencymanager.backend.Mission;

/**
 *
 * @author Kubo
 */
public class MissionsTableModel extends AbstractTableModel{
    private List<Mission> missions = new ArrayList<>();
    private ResourceBundle strings;
    
    public MissionsTableModel(ResourceBundle resourceBundle) {
        strings = resourceBundle;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: 
                return String.class;
            case 1:
                return Date.class;
            case 2:
                return Integer.class;
            case 3:
                return Boolean.class;
            case 4:
                return String.class;
            default:
                throw new IllegalArgumentException(strings.getString("columnIndex_OOB"));
        }
    }
    
    @Override
    public int getRowCount() {
       return missions.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
                 if (rowIndex > missions.size()) {
            throw new IllegalArgumentException(strings.getString("rowIndex_OOB"));
         }
         
         Mission mission = missions.get(rowIndex);
         switch (columnIndex) {
             case 0: 
                 return mission.getCodeName();
             case 1:
                 return mission.getDateCreated();
             case 2:
                 return mission.getMaxNumberOfAgents();
             case 3:
                 return mission.getInProgress();
             case 4:
                 return mission.getNotes();
             default:
                 throw new IllegalArgumentException(strings.getString("columnIndex_OOB"));
         }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Mission mission = null;
        if (rowIndex > missions.size()) {
            throw new IllegalArgumentException(strings.getString("rowIndex_OOB"));
        } else {
            mission = missions.get(rowIndex);
            
            switch (columnIndex) {
                case 0:
                    mission.setCodeName((String) aValue);
                    break;
                case 1:
                    mission.setDateCreated((Date) aValue);
                    break;
                case 2:
                    mission.setMaxNumberOfAgents((Integer) aValue);
                    break;
                case 3:
                    mission.setInProgress((Boolean) aValue);
                    break;
                case 4:
                    mission.setNotes((String) aValue);
                    break;
                default:
                    throw new IllegalArgumentException(strings.getString("columnIndex_OOB"));
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return strings.getString("codeName");
            case 1:
                return strings.getString("dateCreated");
            case 2:
                return strings.getString("maxNumberOfAgents");
            case 3:
                return strings.getString("inProgress");
            case 4:
                return strings.getString("notes");
            default:
                throw new IllegalArgumentException(strings.getString("columnIndex_OOB"));
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return ((columnIndex >= 0) && (columnIndex <= 4));
    }

    void addAll(List<Mission> allMissions) {
        for (Mission mission : missions) {
            this.add(mission);
        }
    }

    void add(Mission mission) {
        missions.add(mission);
        fireTableRowsInserted((missions.size() - 1), missions.size() - 1);
    }
}
