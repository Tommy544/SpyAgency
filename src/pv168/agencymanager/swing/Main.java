/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv168.agencymanager.swing;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import org.apache.commons.dbcp.BasicDataSource;
import pv168.agencymanager.backend.Agent;
import pv168.agencymanager.backend.AgentManagerImpl;
import pv168.agencymanager.backend.Mission;
import pv168.agencymanager.backend.MissionManagerImpl;
import pv168.agencymanager.backend.SpyAgencyManagerImpl;
import pv168.common.DBUtils;
import static pv168.common.DBUtils.date;
import pv168.common.ServiceFailureException;

/**
 *
 * @author vlado
 */
public class Main extends javax.swing.JFrame {

    private ResourceBundle strings = ResourceBundle.getBundle("pv168.agencymanager.swing.Strings", getLocale());
    private DataSource ds;
    private SpyAgencyManagerImpl spyAgencyManager = new SpyAgencyManagerImpl();
    private AgentManagerImpl agentManager = new AgentManagerImpl();
    private MissionManagerImpl missionManager = new MissionManagerImpl();
    public static final Logger logger = Logger.getLogger(Main.class.getName());

    private enum ComboBoxEnum {
        AllAgents, AllMissions; 
    }
    
    /**
     * Creates new form Main
     */
    public Main() {
        //initialize components and data sources
        initComponents();
        jScrollPaneResultTable.setVisible(false);
        ds = prepareDataSource();
        spyAgencyManager.setDataSource(ds);
        agentManager.setDataSource(ds);
        missionManager.setDataSource(ds);

        jTableAgents.setModel(new AgentsTableModel(strings));
        AgentsTableModel model = (AgentsTableModel) jTableAgents.getModel();
        model.add(new Agent(100, "Jozko", false, date("1990-01-10")));
        
        refreshJComboBoxes();
        refreshTable(jTableAgents.getModel());

        // Localization of GUI elements
        jTabbedPane.setTitleAt(0, strings.getString("agents"));
        jTabbedPane.setTitleAt(1, strings.getString("missions"));

        jTabbedPaneOperations.setTitleAt(0, strings.getString("agents"));
        jTabbedPaneOperations.setTitleAt(1, strings.getString("missions"));
        jTabbedPaneOperations.setTitleAt(2, strings.getString("searchAssignments"));
        jTabbedPaneOperations.setTitleAt(3, strings.getString("manageAssignments"));

        jButtonNewAgent.setText(strings.getString("newAgent"));
        jButtonNewMission.setText(strings.getString("newMission"));
        jButtonDeleteAgent.setText(strings.getString("deleteAgent"));
        jButtonDeleteMission.setText(strings.getString("deleteMission"));
        jButtonFindAssignedAgents.setText(strings.getString("findAssignedAgents"));
        jButtonFindAssignedMission.setText(strings.getString("findAssignedMissions"));
        jButtonFindUnassignedAgents.setText(strings.getString("findUnassignedAgents"));
        jButtonFindMissionsWithFreeSpace.setText(strings.getString("findMissionsWithFreeSpace"));
        jButtonRemoveAgentFromMission.setText(strings.getString("removeAgentFromMission"));
        jButtonAssignAgentOnMission.setText(strings.getString("assignAgentOnMission"));

        jComboBoxAssignAgent.setToolTipText(strings.getString("selectAgent"));
        jComboBoxDeleteAgent.setToolTipText(strings.getString("selectAgent"));
        jComboBoxSearchAgent.setToolTipText(strings.getString("selectAgent"));
        jComboBoxAssignMission.setToolTipText(strings.getString("selectMission"));
        jComboBoxDeleteMission.setToolTipText(strings.getString("selectMission"));
        jComboBoxSearchMission.setToolTipText(strings.getString("selectMission"));
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jDialog3 = new javax.swing.JDialog();
        jOptionPane1 = new javax.swing.JOptionPane();
        jTabbedPane = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAgents = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableMissions = new javax.swing.JTable();
        jTabbedPaneOperations = new javax.swing.JTabbedPane();
        jPanelAgents = new javax.swing.JPanel();
        jButtonNewAgent = new javax.swing.JButton();
        jComboBoxDeleteAgent = new javax.swing.JComboBox();
        jButtonDeleteAgent = new javax.swing.JButton();
        jPanelMissions = new javax.swing.JPanel();
        jButtonNewMission = new javax.swing.JButton();
        jComboBoxDeleteMission = new javax.swing.JComboBox();
        jButtonDeleteMission = new javax.swing.JButton();
        jPanelSearchAssignments = new javax.swing.JPanel();
        jButtonFindUnassignedAgents = new javax.swing.JButton();
        jButtonFindMissionsWithFreeSpace = new javax.swing.JButton();
        jLabelSearchAgent = new javax.swing.JLabel();
        jLabelSearchMission = new javax.swing.JLabel();
        jComboBoxSearchAgent = new javax.swing.JComboBox();
        jComboBoxSearchMission = new javax.swing.JComboBox();
        jButtonFindAssignedMission = new javax.swing.JButton();
        jButtonFindAssignedAgents = new javax.swing.JButton();
        jPanelManageAssignments = new javax.swing.JPanel();
        jComboBoxAssignAgent = new javax.swing.JComboBox();
        jComboBoxAssignMission = new javax.swing.JComboBox();
        jLabelSelectAgent = new javax.swing.JLabel();
        jLabelSelectMission = new javax.swing.JLabel();
        jButtonAssignAgentOnMission = new javax.swing.JButton();
        jButtonRemoveAgentFromMission = new javax.swing.JButton();
        jScrollPaneResultTable = new javax.swing.JScrollPane();
        jTableQueryResult = new javax.swing.JTable();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane.setMinimumSize(new java.awt.Dimension(470, 108));

        jTableAgents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                { new Long(1), "James Bond",  new Integer(7), "1980-01-01", null},
                { new Long(2), "Janko Hrasko",  new Integer(100), "2000-02-02",  new Boolean(true)},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Name", "Agent Number", "Date of Enrollment", "Is Dead"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableAgents);

        jTabbedPane.addTab("Agents", jScrollPane1);

        jTableMissions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                { new Long(100), "Stary Otec", "2014-01-01",  new Integer(3),  new Boolean(true), "Get rid of that famous person"},
                { new Long(200), "Jastrab", "2014-03-20",  new Integer(1),  new Boolean(true), "Regulerna sledovacka"},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id", "CodeName", "Date Created", "Max num Agents", "In Progress", "Notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableMissions);

        jTabbedPane.addTab("Missions", jScrollPane2);

        jButtonNewAgent.setText("New Agent");
        jButtonNewAgent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewAgentActionPerformed(evt);
            }
        });

        jComboBoxDeleteAgent.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "James Bond", "Janko Hrasko" }));

        jButtonDeleteAgent.setText("Delete selected Agent");
        jButtonDeleteAgent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAgentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAgentsLayout = new javax.swing.GroupLayout(jPanelAgents);
        jPanelAgents.setLayout(jPanelAgentsLayout);
        jPanelAgentsLayout.setHorizontalGroup(
            jPanelAgentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAgentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelAgentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonNewAgent)
                    .addGroup(jPanelAgentsLayout.createSequentialGroup()
                        .addComponent(jComboBoxDeleteAgent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDeleteAgent)))
                .addContainerGap(211, Short.MAX_VALUE))
        );
        jPanelAgentsLayout.setVerticalGroup(
            jPanelAgentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAgentsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonNewAgent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelAgentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxDeleteAgent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDeleteAgent))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jTabbedPaneOperations.addTab("Agents", jPanelAgents);

        jButtonNewMission.setText("New Mission");
        jButtonNewMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewMissionActionPerformed(evt);
            }
        });

        jComboBoxDeleteMission.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stary Otec", "Jastrab" }));

        jButtonDeleteMission.setText("Delete Selected Mission");

        javax.swing.GroupLayout jPanelMissionsLayout = new javax.swing.GroupLayout(jPanelMissions);
        jPanelMissions.setLayout(jPanelMissionsLayout);
        jPanelMissionsLayout.setHorizontalGroup(
            jPanelMissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMissionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonNewMission)
                    .addGroup(jPanelMissionsLayout.createSequentialGroup()
                        .addComponent(jComboBoxDeleteMission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDeleteMission)))
                .addContainerGap(221, Short.MAX_VALUE))
        );
        jPanelMissionsLayout.setVerticalGroup(
            jPanelMissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMissionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonNewMission)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelMissionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonDeleteMission)
                    .addComponent(jComboBoxDeleteMission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jTabbedPaneOperations.addTab("Missions", jPanelMissions);

        jButtonFindUnassignedAgents.setText("Find Unassigned Agents");

        jButtonFindMissionsWithFreeSpace.setText("Find Missions with free space");

        jLabelSearchAgent.setText("Select Agent:");

        jLabelSearchMission.setText("Select Mission:");

        jComboBoxSearchAgent.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "James Bond", "Janko Hrasko" }));

        jComboBoxSearchMission.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stary Otec", "Jastrab" }));

        jButtonFindAssignedMission.setText("Find Mission assigned to Agent");

        jButtonFindAssignedAgents.setText("Find Agents assigned to Mission");

        javax.swing.GroupLayout jPanelSearchAssignmentsLayout = new javax.swing.GroupLayout(jPanelSearchAssignments);
        jPanelSearchAssignments.setLayout(jPanelSearchAssignmentsLayout);
        jPanelSearchAssignmentsLayout.setHorizontalGroup(
            jPanelSearchAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchAssignmentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSearchAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSearchAssignmentsLayout.createSequentialGroup()
                        .addComponent(jButtonFindUnassignedAgents)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonFindMissionsWithFreeSpace))
                    .addGroup(jPanelSearchAssignmentsLayout.createSequentialGroup()
                        .addGroup(jPanelSearchAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelSearchMission)
                            .addComponent(jLabelSearchAgent))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSearchAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxSearchAgent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxSearchMission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelSearchAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonFindAssignedAgents)
                            .addComponent(jButtonFindAssignedMission))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelSearchAssignmentsLayout.setVerticalGroup(
            jPanelSearchAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchAssignmentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSearchAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonFindUnassignedAgents)
                    .addComponent(jButtonFindMissionsWithFreeSpace))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSearchAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSearchAgent)
                    .addComponent(jComboBoxSearchAgent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFindAssignedMission))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSearchAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSearchMission)
                    .addComponent(jComboBoxSearchMission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFindAssignedAgents))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTabbedPaneOperations.addTab("Search Assignments", jPanelSearchAssignments);

        jComboBoxAssignAgent.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "James Bond", "Janko Hrasko" }));

        jComboBoxAssignMission.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stary Otec", "Jastrab" }));

        jLabelSelectAgent.setText("Select Agent:");

        jLabelSelectMission.setText("Select Mission:");

        jButtonAssignAgentOnMission.setText("Assign Agent on a Mission");

        jButtonRemoveAgentFromMission.setText("Remove Agent from Mission");

        javax.swing.GroupLayout jPanelManageAssignmentsLayout = new javax.swing.GroupLayout(jPanelManageAssignments);
        jPanelManageAssignments.setLayout(jPanelManageAssignmentsLayout);
        jPanelManageAssignmentsLayout.setHorizontalGroup(
            jPanelManageAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelManageAssignmentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelManageAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSelectAgent)
                    .addComponent(jLabelSelectMission))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelManageAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxAssignAgent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxAssignMission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelManageAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonRemoveAgentFromMission)
                    .addComponent(jButtonAssignAgentOnMission))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelManageAssignmentsLayout.setVerticalGroup(
            jPanelManageAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelManageAssignmentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelManageAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxAssignAgent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSelectAgent)
                    .addComponent(jButtonAssignAgentOnMission))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelManageAssignmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxAssignMission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSelectMission)
                    .addComponent(jButtonRemoveAgentFromMission))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jTabbedPaneOperations.addTab("Manage Assignments", jPanelManageAssignments);

        jTableQueryResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Query Result Table"}
            },
            new String [] {
                "null"
            }
        ));
        jScrollPaneResultTable.setViewportView(jTableQueryResult);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPaneOperations)
                    .addComponent(jScrollPaneResultTable)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPaneOperations, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneResultTable, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNewAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewAgentActionPerformed
        NewAgentDialog dialog = new NewAgentDialog(null, true, strings, agentManager);
        dialog.setVisible(true);
        refreshTable(jTableAgents.getModel());
        refreshJComboBoxes();
    }//GEN-LAST:event_jButtonNewAgentActionPerformed

    private void jButtonNewMissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewMissionActionPerformed
        NewMissionDialog dialog = new NewMissionDialog(null, true);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonNewMissionActionPerformed

    private void jButtonDeleteAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAgentActionPerformed
        try {
            String[] strings = ((String) jComboBoxDeleteAgent.getSelectedItem()).split("\\(|\\)");
            Integer number = Integer.parseInt(strings[strings.length - 1]);
            Agent deleteAgent = agentManager.findAgentByAgentNumber(number);
            agentManager.dismissAgent(deleteAgent);
        } catch (ServiceFailureException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        refreshTable(jTableAgents.getModel());
        refreshJComboBoxes();
    }//GEN-LAST:event_jButtonDeleteAgentActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("GTK+".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAssignAgentOnMission;
    private javax.swing.JButton jButtonDeleteAgent;
    private javax.swing.JButton jButtonDeleteMission;
    private javax.swing.JButton jButtonFindAssignedAgents;
    private javax.swing.JButton jButtonFindAssignedMission;
    private javax.swing.JButton jButtonFindMissionsWithFreeSpace;
    private javax.swing.JButton jButtonFindUnassignedAgents;
    private javax.swing.JButton jButtonNewAgent;
    private javax.swing.JButton jButtonNewMission;
    private javax.swing.JButton jButtonRemoveAgentFromMission;
    private javax.swing.JComboBox jComboBoxAssignAgent;
    private javax.swing.JComboBox jComboBoxAssignMission;
    private javax.swing.JComboBox jComboBoxDeleteAgent;
    private javax.swing.JComboBox jComboBoxDeleteMission;
    private javax.swing.JComboBox jComboBoxSearchAgent;
    private javax.swing.JComboBox jComboBoxSearchMission;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JLabel jLabelSearchAgent;
    private javax.swing.JLabel jLabelSearchMission;
    private javax.swing.JLabel jLabelSelectAgent;
    private javax.swing.JLabel jLabelSelectMission;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanelAgents;
    private javax.swing.JPanel jPanelManageAssignments;
    private javax.swing.JPanel jPanelMissions;
    private javax.swing.JPanel jPanelSearchAssignments;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneResultTable;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTabbedPane jTabbedPaneOperations;
    private javax.swing.JTable jTableAgents;
    private javax.swing.JTable jTableMissions;
    private javax.swing.JTable jTableQueryResult;
    // End of variables declaration//GEN-END:variables

    private DataSource prepareDataSource() {
        ResourceBundle dsBundle = ResourceBundle.getBundle("pv168.agencymanager.swing.Database");
        String url = dsBundle.getString("jdbc.url");
        String username = dsBundle.getString("jdbc.username");
        String password = dsBundle.getString("jdbc.password");

        BasicDataSource newDs = new BasicDataSource();
        newDs.setUrl(url);
        newDs.setUsername(username);
        newDs.setPassword(password);

        try {
            //DBUtils.tryCreateTables(newDs, new URL("pv168.agencymanager.backend.createTables.sql"));
            DBUtils.tryCreateTables(newDs, Main.class.getResource("/pv168/agencymanager/backend/createTables.sql"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Error creating tables.", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.SEVERE, "Error creating tables");
            System.exit(1);
        }

        return newDs;
    }
    
    private String[] getJComboBoxValues(ComboBoxEnum mode) throws ServiceFailureException {
        int i = 0;
        String[] resultArray = null;
        
        switch (mode) {
            case AllAgents:
                resultArray = new String[agentManager.getAllAgents().size()];
                for (Agent agent : agentManager.getAllAgents()) {
                    resultArray[i] = agent.getName() + " (" + agent.getAgentNumber() + ")";
                    i++;
                }
                break;
            case AllMissions:
                resultArray = new String[missionManager.getAllMissions().size()];
                for (Mission mission : missionManager.getAllMissions()) {
                    resultArray[i] = mission.getCodeName();
                    i++;
                }
                break;
        }
        
        return (resultArray == null ? new String[0] : resultArray);
    }
    
    private void refreshJComboBoxes() {
        try {
            jComboBoxDeleteAgent.setModel(new DefaultComboBoxModel(getJComboBoxValues(ComboBoxEnum.AllAgents)));
            if (jComboBoxDeleteAgent.getSelectedItem() == null) {
                jButtonDeleteAgent.setEnabled(false);
            } else {
                jButtonDeleteAgent.setEnabled(true);
            }
            jComboBoxDeleteMission.setModel(new DefaultComboBoxModel(getJComboBoxValues(ComboBoxEnum.AllMissions)));
            if (jComboBoxDeleteMission.getSelectedItem() == null) {
                jButtonDeleteMission.setEnabled(false);
            } else {
                jButtonDeleteMission.setEnabled(true);
            }
            jComboBoxAssignAgent.setModel(new DefaultComboBoxModel(getJComboBoxValues(ComboBoxEnum.AllAgents)));
            if (jComboBoxAssignAgent.getSelectedItem() == null) {
                jButtonAssignAgentOnMission.setEnabled(false);
                jButtonRemoveAgentFromMission.setEnabled(false);
            } else {
                jButtonAssignAgentOnMission.setEnabled(true);
                jButtonRemoveAgentFromMission.setEnabled(true);
            }
            jComboBoxAssignMission.setModel(new DefaultComboBoxModel(getJComboBoxValues(ComboBoxEnum.AllMissions)));
             if (jComboBoxAssignMission.getSelectedItem() == null) {
                jButtonAssignAgentOnMission.setEnabled(false);
                jButtonRemoveAgentFromMission.setEnabled(false);
            } else {
                jButtonAssignAgentOnMission.setEnabled(true);
                jButtonRemoveAgentFromMission.setEnabled(true);
            }
            jComboBoxSearchAgent.setModel(new DefaultComboBoxModel(getJComboBoxValues(ComboBoxEnum.AllAgents)));
            if (jComboBoxSearchAgent.getSelectedItem() == null) {
                jButtonFindAssignedMission.setEnabled(false);
            } else {
                jButtonFindAssignedMission.setEnabled(true);
            }
            jComboBoxSearchMission.setModel(new DefaultComboBoxModel(getJComboBoxValues(ComboBoxEnum.AllMissions)));
            if (jComboBoxSearchMission.getSelectedItem() == null) {
                jButtonFindAssignedAgents.setEnabled(false);
            } else {
                jButtonFindAssignedAgents.setEnabled(true);
            }
        } catch (ServiceFailureException ex) {
            logger.log(Level.SEVERE, "Exception while refreshing combo boxes.", ex);
        }
    }
    
    private void refreshTable(TableModel model) {
        if (model instanceof AgentsTableModel) {
            try {
                //AgentsTableModel tm = (AgentsTableModel) model;
                AgentsTableModel tm = new AgentsTableModel(strings);
                tm.addAll(agentManager.getAllAgents());
                jTableAgents.setModel(tm);
            } catch (ServiceFailureException ex) {
                logger.log(Level.SEVERE, "Exception while refreshing Agents table.", ex);
            }
        } else if (model instanceof MissionsTableModel) {
            try {
                //MissionsTableModel tm = (MissionsTableModel) model;
                MissionsTableModel tm = new MissionsTableModel(strings);
                tm.addAll(missionManager.getAllMissions());
                jTableMissions.setModel(tm);
            } catch (ServiceFailureException ex) {
                logger.log(Level.SEVERE, "Exception while refreshing Missions table.", ex);
            }
        }
    }
}
