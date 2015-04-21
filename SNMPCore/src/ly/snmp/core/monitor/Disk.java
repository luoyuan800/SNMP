/*
 * Disk.java
 * Date: 4/15/2015
 * Time: 4:23 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.monitor;

import ly.snmp.core.model.DataSet;
import ly.snmp.core.model.Oid;
import ly.snmp.core.model.TableColumnOid;
import ly.snmp.core.model.TableOid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Disk implements Monitor {
    private Double allocationUnits;
    private String index;
    private Map<String, TableColumnOid> oids;
    private TableOid table;
    private DataSet<Double> used = new DataSet<Double>("Used"), totalSize = new DataSet<Double>("Total"), utilization = new DataSet<Double>("Util");
    private Map<String,LogicalDisk> logicalDisks = new HashMap<String, LogicalDisk>();

    public Disk() {
        oids = new HashMap<String, TableColumnOid>(5);
        table = new TableOid("1.3.6.1.2.1.25.2.3", "1.3.6.1.2.1.25.2.3.1.2",
                "1.3.6.1.2.1.25.2.3.1.3", "1.3.6.1.2.1.25.2.3.1.4", "1.3.6.1.2.1.25.2.3.1.5",
                "1.3.6.1.2.1.25.2.3.1.6");
        for (TableColumnOid column : table.getColumns()) {
            oids.put(column.getOidString(), column);
        }
    }

    @Override
    public Set<Oid> getOIDs() {
        HashSet<Oid> set = new HashSet<Oid>();
        set.add(table);
        return set;
    }

    @Override
    public void build(Long time) {
        totalSize.appendData(time, buildTotalSize(time));
        used.appendData(time, buildUsed(time));
    }

    private List<String> findIndex() {
        List<String> indexs = new ArrayList<String>();
        this.index = "";
        TableColumnOid type = oids.get("1.3.6.1.2.1.25.2.3.1.2");
        for (String index : type.getIndex()) {
            if (type.<String>getValue(index).equals("1.3.6.1.2.1.25.2.1.4")) {
                indexs.add(index);
            }
        }
        return indexs;
    }

    public double buildUsed(Long time) {
        double totalUsed = 0;
        for (String index : findIndex()) {
            double alloc = getAllocUnit(index);
            TableColumnOid used = oids.get("1.3.6.1.2.1.25.2.3.1.6");
            TableColumnOid names = oids.get("1.3.6.1.2.1.25.2.3.1.3");
            Double usedBlock = used.getValue(index);
            LogicalDisk logicalDisk = getLogicalDisk(index, names);
            if (usedBlock != null) {
                double v = usedBlock * alloc;
                logicalDisk.setUsed(v,time);
                totalUsed = totalUsed + v;
            }
        }
        return totalUsed;
    }

    private LogicalDisk getLogicalDisk(String index, TableColumnOid names) {
        LogicalDisk logicalDisk = logicalDisks.get(index);
        if(logicalDisk==null){
            logicalDisk = new LogicalDisk(names.<String>getValue(index));
            logicalDisks.put(index, logicalDisk);
        }
        return logicalDisk;
    }

    public double buildTotalSize(Long time) {
        double total = 0;
        for (String index : findIndex()) {
            double alloc = getAllocUnit(index);
            TableColumnOid names = oids.get("1.3.6.1.2.1.25.2.3.1.3");
            TableColumnOid tol = oids.get("1.3.6.1.2.1.25.2.3.1.5");
            Double usedBlock = tol.getValue(index);
            LogicalDisk logicalDisk = getLogicalDisk(index, names);
            if (usedBlock != null) {
                double v = usedBlock * alloc;
                logicalDisk.setSize(v, time);
                total = total + v;
            }
        }
        return total;
    }

    private double getAllocUnit(String index) {
        TableColumnOid alloc = oids.get("1.3.6.1.2.1.25.2.3.1.4");
        allocationUnits = alloc.getValue(index);
        return allocationUnits;
    }

    public DataSet<Double> getUsed() {
        return used;
    }

    public DataSet<Double> getTotalSize() {
        return totalSize;
    }

    public Collection<LogicalDisk> getLogicalDisk(){
        return logicalDisks.values();
    }

    public class LogicalDisk {
        private String name;
        private DataSet<Double> size, used;

        public LogicalDisk(String name) {
            if(name!=null) {
                this.name = name;
            }else{
                this.name = "";
            }
            size = new DataSet<Double>("Size");
            used = new DataSet<Double>("Used");
        }

        public String getName() {
            return name;
        }

        public DataSet<Double> getSize() {
            return size;
        }

        public DataSet<Double> getUsed() {
            return used;
        }

        public void setSize(Double size, Long time){
            this.size.appendData(time, size);
        }
        public void setUsed(Double used, Long time){
            this.used.appendData(time, used);
        }
    }
}
