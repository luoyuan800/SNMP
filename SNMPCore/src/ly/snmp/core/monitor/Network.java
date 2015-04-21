/*
 * Network.java
 * Date: 4/20/2015
 * Time: 8:38 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.monitor;

import ly.snmp.core.model.DataSet;
import ly.snmp.core.model.Oid;
import ly.snmp.core.model.TableColumnOid;
import ly.snmp.core.model.TableOid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Network implements Monitor {
    private Map<String, Interface> interfaces;
    private Map<String, Interface> sample;
    private static final long UINT_MAX_VALUE = 4294967295l;
    private static final String IF_INDEX = "1.3.6.1.2.1.2.2.1.1";// ifTable.ifIndex
    private static final String IF_TYPE = "1.3.6.1.2.1.2.2.1.3";// ifTable.ifType
    protected static final String IF_DESCR = "1.3.6.1.2.1.2.2.1.2";// ifTable.ifDescr
    private static final String IF_MTU = "1.3.6.1.2.1.2.2.1.4";// ifTable.ifMtu
    private static final String IF_SPEED = "1.3.6.1.2.1.2.2.1.5";// ifTable.ifSpeed
    private static final String IF_PHY_ADDRESS = "1.3.6.1.2.1.2.2.1.6";// ifTable.ifPhysAddress
    private static final String IF_LAST_CHANGE = "1.3.6.1.2.1.2.2.1.9";// ifTable.ifLastChange
    private static final String IF_IN_OCTETS = "1.3.6.1.2.1.2.2.1.10"; // ifTable.ifInOctets
    private static final String IF_OUT_OCTETS = "1.3.6.1.2.1.2.2.1.16"; // ifTable.ifOutOctets
    private static final String IP_AD_ENT_ADDR = "1.3.6.1.2.1.4.20.1.1";
    private static final String IP_AD_ENT_IF_INDEX = "1.3.6.1.2.1.4.20.1.2";// ipAddrTable.ipAdEntIfIndex
    private Map<String, TableColumnOid> tableColumnOidMap;
    private Set<Oid> oids;
    private DataSet<Double> inRate, outRate;

    public Network() {
        oids = new HashSet<Oid>(Arrays.asList(new TableOid("1.3.6.1.2.1.2.2", IF_INDEX, IF_TYPE, IF_DESCR, IF_MTU, IF_SPEED, IF_PHY_ADDRESS, IF_LAST_CHANGE, IF_IN_OCTETS, IF_OUT_OCTETS),
                new TableOid("1.3.6.1.2.1.4.20", IP_AD_ENT_ADDR, IP_AD_ENT_IF_INDEX)));
        for (Oid oid : oids) {
            for (TableColumnOid column : ((TableOid) oid).getColumns()) {
                tableColumnOidMap.put(column.getOidString(), column);
            }
        }
        interfaces = new HashMap<String, Interface>();
        sample = new HashMap<String, Interface>();
    }

    @Override
    public Set<Oid> getOIDs() {
        return oids;
    }

    @Override
    public void build(Long time) {
        for (String index : tableColumnOidMap.get(IF_DESCR).getIndex()) {
            String des = tableColumnOidMap.get(IF_DESCR).getValue(index);
            Double inOcts = tableColumnOidMap.get(IF_IN_OCTETS).getValue(index);
            Double outOcts = tableColumnOidMap.get(IF_OUT_OCTETS).getValue(index);
            Double speed = tableColumnOidMap.get(IF_SPEED).getValue(index);
            Double mtu = tableColumnOidMap.get(IF_MTU).getValue(index);
            String phy = tableColumnOidMap.get(IF_PHY_ADDRESS).getValue(index);
            Long change = tableColumnOidMap.get(IF_LAST_CHANGE).getValue(index);
            Interface anInterface = sample.get(index);
            if (anInterface != null) {
                Interface instance = interfaces.get(index);
                if (instance == null) {
                    instance = anInterface;
                    interfaces.put(index, anInterface);
                }
                long mill = System.currentTimeMillis() - anInterface.mill;
                instance.inRate.appendData(time, calculateRateWithOverflowCheck(inOcts, anInterface.inOcts, mill, 1000d * 1000d, true));
                instance.outRate.appendData(time, calculateRateWithOverflowCheck(outOcts, anInterface.outOcts, mill, 1000d * 1000d, true));
                instance.phyAddress = phy;
                instance.mtu = mtu;
                instance.speed = speed;
                instance.desc = des;
                instance.ip = getIP(index);
            } else {
                anInterface = new Interface();
                sample.put(index, anInterface);
            }
            anInterface.change = change;
            anInterface.inOcts = inOcts;
            anInterface.outOcts = outOcts;
            anInterface.mill = System.currentTimeMillis();
        }
    }

    private String getIP(String index) {
        String ipIndex = "";
        for (String key : tableColumnOidMap.get(IP_AD_ENT_IF_INDEX).getIndex()) {
            if (index.equals(tableColumnOidMap.get(IP_AD_ENT_IF_INDEX).getValue(key))) {
                ipIndex = key;
                break;
            }
        }
        return tableColumnOidMap.get(IP_AD_ENT_ADDR).getValue(ipIndex);
    }

    private Double calculateRateWithOverflowCheck(Double current, Double previous, Long mill, Double unit, boolean doNotGuess) {
        Long seconds = mill / 1000;
        if (current == null || previous == null || current == Double.MAX_VALUE || previous == Double.MAX_VALUE) {
            return 0d;
        }
        double duration = 0;
        if (doNotGuess) {
            duration = current >= previous ? (current - previous) : 0d;
        } else {
            if (current < UINT_MAX_VALUE && previous < UINT_MAX_VALUE) {
                current = (double) ((previous.longValue() & 0xFFFFFFFF00000000l) | current.longValue());
            } else if (current > UINT_MAX_VALUE && previous < UINT_MAX_VALUE && (current - previous) > UINT_MAX_VALUE) {
                previous = (double) ((current.longValue() & 0xFFFFFFFF00000000l) | previous.longValue());
            } else if (current < previous && current < UINT_MAX_VALUE && previous < UINT_MAX_VALUE) {
                current = current + UINT_MAX_VALUE;
            }
            if (current >= previous) {
                duration = current - previous;
            }
        }
        return duration / (seconds * unit);
    }

    public class Interface {
        private double inOcts;
        private double outOcts;
        private double mtu;
        private double speed;
        private String desc;
        private Long change;
        private String ip;
        private String phyAddress;
        private DataSet<Double> inRate, outRate;
        private Long mill;


        public double getMtu() {
            return mtu;
        }

        public double getSpeed() {
            return speed;
        }

        public String getDesc() {
            return desc;
        }

        public DataSet<Double> getInRate() {
            return inRate;
        }

        public DataSet<Double> getOutRate() {
            return outRate;
        }
    }
}
