/*
 * DataSet.java
 * Date: 4/13/2015
 * Time: 9:04 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.model;

import ly.snmp.core.policy.DefaultPolicy;
import ly.snmp.core.policy.Policy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataSet<T extends Number> {
    private Map<Long, T> date;
    private Policy policy;
    private static HashSet<DataSet> dataSets = new HashSet<DataSet>();
    private String name;

    public static Set<DataSet> getAllDataSetInstances() {
        return dataSets;
    }

    public DataSet(String name) {
        this.date = new HashMap<Long, T>();
        this.policy = new DefaultPolicy();
        this.name = name;
        dataSets.add(this);
    }

    public void appendData(Long time, T data) {
        this.date.put(time, data);
    }

    public T getData(Long time) {
        return date.get(time);
    }

    public Set<Long> getTimes() {
        return date.keySet();
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public void rollUp() {
        if (getTimes().size() > 6) {
            int count = 1;
            Number wait4Rol = 0;
            for (Long time : date.keySet()) {
                count++;
                if (count > 6) {
                    T value = date.get(time);
                }
            }
        }
    }

    public String getName() {
        return name;
    }
}
