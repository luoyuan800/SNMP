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

/**
 * This Class use for store those num value which may change frequently<br>
 * It will store num value and the time stamp.
 * @param <T> Should be Double, Integer, Float, Long.
 */
public class DataSet<T extends Number> {
    private Map<Long, T> date;
    private Policy policy;
    private static HashSet<DataSet> dataSets = new HashSet<DataSet>();
    private String name;
    private T latest;

    /**
     * A static method for get all the instances create for this class.
     * @return
     */
    public static Set<DataSet> getAllDataSetInstances() {
        return dataSets;
    }

    /**
     * Create a data set with identifying name.
     * @param name Identification
     */
    public DataSet(String name) {
        this.date = new HashMap<Long, T>();
        this.policy = new DefaultPolicy();
        this.name = name;
        dataSets.add(this);
    }

    /**
     * Add a value into this data set
     * @param time the time this value get
     * @param data the data value
     */
    public void appendData(Long time, T data) {
        this.date.put(time, data);
        this.latest = data;
    }

    /**
     * Get value by time
     * @param time the time those value save in this data set
     * @return the value
     */
    public T getData(Long time) {
        return date.get(time);
    }

    /**
     * Get all the time stamp which save in this data set
     * @return All the time(belong to value)
     */
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
    
    public T getLatestData(){
    	return latest;
    }
}
