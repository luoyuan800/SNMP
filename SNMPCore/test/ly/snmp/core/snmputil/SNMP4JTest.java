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
import ly.snmp.core.model.TableColumnOid;
import ly.snmp.core.model.TableOid;
import ly.snmp.core.monitor.CPU;
import ly.snmp.core.monitor.Disk;
import ly.snmp.core.monitor.Memory;
import ly.snmp.core.monitor.Network;
import ly.snmp.core.monitor.SystemInfo;

import java.io.IOException;

public class SNMP4JTest {
    public static void main(String... args) throws IOException {

        SNMPParameter parameter = new SNMPParameter();
        parameter.setCommunity("public");
        parameter.setVersion(SNMPVersion.V2C);
        parameter.setPort(161);
        parameter.setIp("10.4.117.223");
        SNMP snmp4JTcp = new SNMP4JTCP(parameter);
        Oid oid = snmp4JTcp.getTable(new TableOid("1.3.6.1.2.1.25.2.3", "1.3.6.1.2.1.25.2.3.1.5"));
        for(TableColumnOid column : ((TableOid)oid).getColumns()){
            for(String index : column.getIndex()){
                System.out.println(column.getValue(index));
            }
        }
        System.out.println("----");
        SNMP snmp4J = new SNMP4J(parameter);
        oid = snmp4J.getTable(new TableOid("1.3.6.1.2.1.25.2.3", "1.3.6.1.2.1.25.2.3.1.5"));
        for(TableColumnOid column : ((TableOid)oid).getColumns()){
            for(String index : column.getIndex()){
                System.out.println(column.getValue(index));
            }
        }

        /*Device device = new Device("10.4.115.85");
        Oid oid = new Oid("1.3.6.1.2.1.2.1");
        device.addOids(oid);
        device.initDevice(parameter);
        TableOid table = new TableOid("1.3.6.1.2.1.25.2.3", "1.3.6.1.2.1.25.2.3.1.5");
        device.addOids(table);
        device.doCollection();
        for(TableColumnOid column : table.getColumns()){
            for(String index : column.getIndex()){
                System.out.println(column.getValue(index));
            }
        }*/


        /*CPU cpu = device.getMonitor(CPU.class);
        Memory memory = device.getMonitor(Memory.class);
        Disk disk = device.getMonitor(Disk.class);*/
        /*try {
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
        */


    }
}
