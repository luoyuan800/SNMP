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
    private SNMPVersion version;
    private String ip;
    private String community;
    private int port;
    private SNMP snmp;
    private List<Monitor> monitors;

    public Device(String ip, SNMPVersion v) {
        this.ip = ip;
        this.version = v;
        this.port = 161;
        this.monitors = new ArrayList<Monitor>();
    }

    public void initDevice() throws IOException {
        if (community == null || port == 0) {
            throw new IllegalArgumentException("Please set the port and community String");
        }
        snmp = new SNMP4J(this);
    }

    public void doCollection() {
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
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public SNMPVersion getVersion() {
        return version;
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
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
}
