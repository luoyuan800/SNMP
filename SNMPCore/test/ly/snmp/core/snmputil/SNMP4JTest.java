/*
 * SNMP4JTest.java
 * Date: 4/1/2015
 * Time: 9:50 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.snmputil;


import ly.snmp.core.model.Device;
import ly.snmp.core.model.Oid;
import ly.snmp.core.model.Protocol;
import ly.snmp.core.model.SNMPParameter;
import ly.snmp.core.model.SNMPVersion;
import ly.snmp.core.model.TableOid;
import ly.snmp.core.monitor.CPU;
import ly.snmp.core.monitor.Disk;
import ly.snmp.core.monitor.Memory;
import ly.snmp.core.monitor.Network;
import ly.snmp.core.monitor.SystemInfo;

public class SNMP4JTest {
    public static void main(String... args) {
        try {
            Device device = new Device("10.30.176.177");
            //TableOid table = new TableOid("1.3.6.1.2.1.2.2", "1.3.6.1.2.1.2.2.1.2","1.3.6.1.2.1.2.2.1.5","1.3.6.1.2.1.2.2.1.3");
            //device.addOids(table);
            //device.addMonitor(new Network());
            Oid oid = new Oid("1.3.6.1.2.1.2.1");
            device.addOids(oid);
            SNMPParameter parameter = new SNMPParameter();
            parameter.setUserName("shaDes");
            parameter.setPort(161);
            parameter.setVersion(SNMPVersion.V3);
            parameter.setAuthentication("1234567890");
            parameter.setPrivacy("1234567890");
            parameter.setAuthProtocol(Protocol.AuthSHA);
            parameter.setPrivacyProtocol(Protocol.PrivDES);
            device.initDevice(parameter);
            //device.addMonitor(new CPU());
            device.doCollection();
            System.out.print(device.getOids());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
