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

    void addAll(List<Mission> allMissions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
