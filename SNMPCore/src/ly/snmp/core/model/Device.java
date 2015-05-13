package ly.snmp.core.model;

import ly.snmp.core.monitor.Monitor;
import ly.snmp.core.snmputil.SNMP;
import ly.snmp.core.snmputil.SNMP4J;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Device implements Runnable {
    private List<Oid> oids;
    private Map<String, Oid> oidMap;
    private String ip;
    private SNMP snmp;
    private List<Monitor> monitors;
    private SNMPParameter snmpParameter;

    public Device(String ip) {
        this.ip = ip;
        this.monitors = new ArrayList<Monitor>();
    }

    public Device(String ip, SNMPVersion version){

    }


    public void initDevice(){

    }
    public void initDevice(SNMPParameter snmpParameter) throws IOException {
        if (snmpParameter == null || snmpParameter.getCommunity() == null) {
            throw new IllegalArgumentException("Please initialize the snmp parameter!");
        }
        snmpParameter.setIp(ip);
        this.snmpParameter = snmpParameter;
        snmp = new SNMP4J(this.snmpParameter);
    }

    public void initDevice(SNMP snmp){
        this.snmp = snmp;
    }

    public void doCollection() {
        if(snmp == null){
            throw new IllegalArgumentException("Please initialize the snmp parameter by call the method Device.initDevice(SNMPParameter)!");
        }
        List<Oid> oids4C = new ArrayList<Oid>();
        if (oids != null) {
            oids4C.addAll(oids);
            oidMap = new HashMap<String, Oid>(oids.size());
            for (Oid oid : oids) {
                oidMap.put(oid.getOidString(), oid);
            }
        }
        if (monitors != null) {
            for (Monitor monitor : monitors) {
                oids4C.addAll(monitor.getOIDs());
            }
        }
        Oid[] oidArray = new Oid[oids4C.size()];
        snmp.get(oids4C.toArray(oidArray));
        for (Monitor monitor : monitors) {
            monitor.build(System.currentTimeMillis());
        }
    }

    public String getIp() {
        return ip;
    }

    public String getCommunity() {
        return snmpParameter.getCommunity();
    }

    public void setCommunity(String community) {
        this.snmpParameter.setCommunity(community);
    }

    public SNMPVersion getVersion() {
        return this.snmpParameter.getVersion();
    }

    public List<Oid> getOids() {
        return oids;
    }

    public void addOids(Oid... oids) {
        if (oids != null) {
            if (oids.length > 0 && this.oids == null) {
                this.oids = new ArrayList<Oid>(oids.length);
            }
            Collections.addAll(this.oids, oids);
        }
    }

    public int getPort() {
        return this.snmpParameter.getPort();
    }

    public void setPort(int port) {
        this.snmpParameter.setPort(port);
    }

    public Map<String, Oid> getOidMap() {
        return oidMap;
    }

    public void addMonitor(Monitor... monitors) {
        Collections.addAll(this.monitors, monitors);
    }

    public <T> T getMonitor(Class monitorType) {
        for (Monitor monitor : monitors) {
            if (monitor.getClass().equals(monitorType)) {
                return (T) monitor;
            }
        }
        return null;
    }

    @Override
    public void run() {
        doCollection();
    }

    public SNMPParameter getSnmpParameter() {
        return snmpParameter;
    }
}
