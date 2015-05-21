/*
 * DeviceSample.java
 * Date: 5/21/2015
 * Time: 2:18 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.sample;

import ly.snmp.core.model.Device;
import ly.snmp.core.model.Oid;
import ly.snmp.core.model.SNMPParameter;
import ly.snmp.core.model.SNMPVersion;
import ly.snmp.core.monitor.Memory;
import ly.snmp.core.monitor.Monitor;

import java.io.IOException;

/**
 * This is a sample for how to use this snmp framework to collect snmp data
 */
public class DeviceSample {
    public static final String ip = "127.0.0.1";
    public static void main(String...args) throws IOException {
        Device device = new Device(ip);
        SNMPParameter parameter = new SNMPParameter();
        parameter.setVersion(SNMPVersion.V1);
        //parameter.setPort(163);//Default is 163
        parameter.setCommunity("public");
        device.initDevice(parameter);
        //Will collect memory data
        Monitor monitor = new Memory();
        device.addMonitor(monitor);
        //Add special oid for collect
        Oid oid = new Oid("1.3.6.1.2.1.1.1");
        device.addOids(oid);
        device.doCollection();
        /*//Now we can get those snmp data from device
        Memory memory = device.getMonitor(Memory.class);
        System.out.println(memory.getTotalSize());
        System.out.println(memory.getUsed());
        System.out.println(oid.getValue());
        */
    }
}
