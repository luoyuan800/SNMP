/*
 * Memory.java
 * Date: 4/9/2015
 * Time: 4:01 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.monitor;

import ly.snmp.core.model.DataSet;
import ly.snmp.core.model.Oid;
import ly.snmp.core.model.TableColumnOid;
import ly.snmp.core.model.TableOid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Collect Memory by using 1.3.6.1.2.1.25.2.3 which type equals 1.3.6.1.2.1.25.2.1.2(Ram)
 */
public class Memory implements Monitor {
    private Double allocationUnits;
    private String index;
    Map<String, TableColumnOid> oids;
    TableOid table;
    private DataSet<Double> used = new DataSet<Double>(" "), totalSize = new DataSet<Double>(" ");

    public Memory() {
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
        totalSize.appendData(time, buildTotalSize());
        used.appendData(time, buildUsed());
    }

    private String findIndex() {
        if (this.index == null) {
            this.index = "";
            TableColumnOid type = oids.get("1.3.6.1.2.1.25.2.3.1.2");
            for (String index : type.getIndex()) {
                if (type.<String>getValue(index).equals("1.3.6.1.2.1.25.2.1.2")) {
                    this.index = index;
                }
            }
        }
        return this.index;
    }

    private double buildUsed() {
        double alloc = getAllocUnit();
        TableColumnOid used = oids.get("1.3.6.1.2.1.25.2.3.1.6");
        Double usedBlock = used.getValue(findIndex());
        if (usedBlock != null) {
            return usedBlock * alloc;
        } else {
            return 0d;
        }
    }

    private double buildTotalSize() {
        double alloc = getAllocUnit();
        TableColumnOid used = oids.get("1.3.6.1.2.1.25.2.3.1.5");
        Double size = used.getValue(findIndex());
        if (size != null) {
            return size * alloc;
        } else {
            return 0d;
        }
    }

    private double getAllocUnit() {
        if (allocationUnits == null) {
            String index = findIndex();
            TableColumnOid alloc = oids.get("1.3.6.1.2.1.25.2.3.1.4");
            allocationUnits = alloc.getValue(index);
        }
        return allocationUnits;
    }

    public DataSet<Double> getUsed(){
        return used;
    }
    public DataSet<Double> getTotalSize(){
        return totalSize;
    }
}
