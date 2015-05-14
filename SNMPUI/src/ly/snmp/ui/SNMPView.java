
package ly.snmp.ui;

import ly.snmp.core.model.DataSet;
import ly.snmp.core.model.Device;
import ly.snmp.core.monitor.CPU;
import ly.snmp.core.monitor.Disk;
import ly.snmp.core.monitor.Memory;
import ly.snmp.core.monitor.Network;
import ly.snmp.core.service.SNMPManager;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author gluo
 */
public class SNMPView extends javax.swing.JFrame {


    public void setCpuChart(DataSet<Double> dataSet){
        cpuChart.setData(dataSet);
        cpuChart.repaint();
    }
    public void setMemoryChart(DataSet<Double> dataSet){
        memoryChart.setData(dataSet);
        memoryChart.repaint();
    }
    public void setDiskChart(DataSet<Double> dataSet){
        diskChart.setData(dataSet);
        diskChart.repaint();
    }
    public void setNetworkChart(DataSet<Double> dataSet){
        networkChart.setData(dataSet);
        networkChart.repaint();
    }
    /**
     * Creates new form SNMPView
     */
    public SNMPView() {
        initComponents();
    }

    public SNMPView(SNMPManager manager){
        super();
        setManager(manager);
        initComponents();
        start();
    }

    private void start(){
        runQuery = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (runQuery){
                    List<Device> devices = manager.getDevices();
                    final String[] ips = new String[devices.size()];
                    for(int i=0;i<ips.length;i++){
                        ips[i]= devices.get(i).getIp();
                    }
                    devicesList.setModel(new javax.swing.AbstractListModel() {
                        String[] strings = ips;

                        public int getSize() {
                            return strings.length;
                        }

                        public Object getElementAt(int i) {
                            return strings[i];
                        }
                    });
                    try {
                        Thread.sleep(1000*5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void initComponents() {
        containAll = new javax.swing.JScrollPane();
        devicesList = new javax.swing.JList();
        devicesList.setSize(100,500);
        separator4Devices = new javax.swing.JSeparator();
        separator4CPUMemory = new javax.swing.JSeparator();
        separator4DiskNetwork = new javax.swing.JSeparator();
        additionMsg = new javax.swing.JLabel();
        cpuChart = new Chart("CPU", 200, 200);
        memoryChart =new Chart("Memory", 200, 200);
        diskChart = new Chart("Disk", 200, 200);
        networkChart = new Chart("Network", 200, 200);
        menus = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        add = new javax.swing.JMenuItem();
        deleteDevice = new javax.swing.JMenuItem();
        start = new javax.swing.JMenuItem();
        stop = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        containAll.setViewportView(devicesList);
        devicesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                devicesListItemSelect(event);
            }
        });

        separator4CPUMemory.setOrientation(javax.swing.SwingConstants.VERTICAL);

        separator4DiskNetwork.setOrientation(javax.swing.SwingConstants.VERTICAL);

        additionMsg.setText("jLabel1");

//        javax.swing.GroupLayout cpuChartLayout = new javax.swing.GroupLayout(cpuChart);
//        cpuChart.setLayout(cpuChartLayout);
//        cpuChartLayout.setHorizontalGroup(
//            cpuChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 142, Short.MAX_VALUE)
//        );
//        cpuChartLayout.setVerticalGroup(
//            cpuChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 130, Short.MAX_VALUE)
//        );
//
//        javax.swing.GroupLayout memoryChartLayout = new javax.swing.GroupLayout(memoryChart);
//        memoryChart.setLayout(memoryChartLayout);
//        memoryChartLayout.setHorizontalGroup(
//            memoryChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 159, Short.MAX_VALUE)
//        );
//        memoryChartLayout.setVerticalGroup(
//            memoryChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 0, Short.MAX_VALUE)
//        );
//
//        javax.swing.GroupLayout diskChartLayout = new javax.swing.GroupLayout(diskChart);
//        diskChart.setLayout(diskChartLayout);
//        diskChartLayout.setHorizontalGroup(
//            diskChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 141, Short.MAX_VALUE)
//        );
//        diskChartLayout.setVerticalGroup(
//            diskChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 0, Short.MAX_VALUE)
//        );
//
//        javax.swing.GroupLayout networkChartLayout = new javax.swing.GroupLayout(networkChart);
//        networkChart.setLayout(networkChartLayout);
//        networkChartLayout.setHorizontalGroup(
//            networkChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 162, Short.MAX_VALUE)
//        );
//        networkChartLayout.setVerticalGroup(
//            networkChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 130, Short.MAX_VALUE)
//        );

        jMenu1.setText("File");

        add.setText("Add Device");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDevice(e);
            }
        });
        jMenu1.add(add);

        deleteDevice.setText("Delete");
        deleteDevice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteDeviceActionPerformed(evt);
            }
        });
        jMenu1.add(deleteDevice);

        start.setText("Start Cllection");
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });
        jMenu1.add(start);

        stop.setText("Stop Collection");
        jMenu1.add(stop);

        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu1.add(exit);

        menus.add(jMenu1);

        setJMenuBar(menus);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(additionMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(containAll, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(diskChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(separator4DiskNetwork, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(networkChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cpuChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(separator4CPUMemory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(memoryChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(separator4Devices, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(containAll, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(separator4CPUMemory, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cpuChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(memoryChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(separator4Devices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(separator4DiskNetwork, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(networkChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 8, Short.MAX_VALUE))
                                    .addComponent(diskChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(additionMsg, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE))
        );

        //pack();
        setSize(550,520);
        setResizable(false);
    }// </editor-fold>//GEN-END:initComponents

    private void addDevice(ActionEvent e) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final AddDeivce dialog = new AddDeivce(view, true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private void devicesListItemSelect(ListSelectionEvent event) {
        String ip = (String)(devicesList.getSelectedValue());
        List<Device> devices = manager.getDevices();
        for(Device device : devices){
            if(device.getIp().equals(ip)){
                CPU cpu = device.getMonitor(CPU.class);
                Memory memory = device.getMonitor(Memory.class);
                Disk disk = device.getMonitor(Disk.class);
                Network network = device.getMonitor(Network.class);
                setCpuChart(cpu.getUtilization());
                setMemoryChart(memory.getUsed());
                setDiskChart(disk.getUsed());
                setNetworkChart(network.getInRate());
                break;
            }
        }
        view.repaint();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void deleteDeviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteDeviceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteDeviceActionPerformed

    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exitActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SNMPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SNMPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SNMPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SNMPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SNMPView view = new SNMPView();
                DataSet<Double> dataSet = new DataSet<Double>(" ");
                dataSet.appendData(123456l, 1.0);
                dataSet.appendData(1234533l, 2.0);
                view.setDiskChart(dataSet);
                view.setCpuChart(dataSet);
                view.setNetworkChart(dataSet);
                view.setMemoryChart(dataSet);
                view.setVisible(true);
            }
        });

    }

    private javax.swing.JMenuItem add;
    private javax.swing.JLabel additionMsg;
    private javax.swing.JScrollPane containAll;
    private Chart cpuChart;
    private javax.swing.JMenuItem deleteDevice;
    private javax.swing.JList devicesList;
    private Chart diskChart;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenu jMenu1;
    private Chart memoryChart;
    private javax.swing.JMenuBar menus;
    private Chart networkChart;
    private javax.swing.JSeparator separator4CPUMemory;
    private javax.swing.JSeparator separator4Devices;
    private javax.swing.JSeparator separator4DiskNetwork;
    private javax.swing.JMenuItem start;
    private javax.swing.JMenuItem stop;
    private SNMPManager manager;
    private boolean runQuery;
    private final SNMPView view = this;

    public SNMPManager getManager() {
        return manager;
    }

    public void setManager(SNMPManager manager) {
        this.manager = manager;
    }

}
