/*
 * TestSNMPManager.java
 * Date: 4/15/2015
 * Time: 3:28 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.service;

import ly.snmp.core.model.Device;
import ly.snmp.core.model.SNMPVersion;
import ly.snmp.core.model.TableOid;
import ly.snmp.core.monitor.CPU;
import ly.snmp.core.monitor.Disk;
import ly.snmp.core.monitor.Memory;
import ly.snmp.core.monitor.Network;

public class TestSNMPManager {
    public static void main(String... args) {
        SNMPManager manager = SNMPManager.getInstance();
        Device device = new Device("10.30.178.28", SNMPVersion.V1);
        device.setCommunity("public");
        device.setPort(161);
        TableOid table = new TableOid("1.3.6.1.2.1.2.2", "1.3.6.1.2.1.2.2.1.2", "1.3.6.1.2.1.2.2.1.5", "1.3.6.1.2.1.2.2.1.3");
        device.addOids(table);
        device.addMonitor(new Memory(), new Disk(), new CPU(), new Network());
        device.initDevice();

        manager.addDevice(device);
    }
}
