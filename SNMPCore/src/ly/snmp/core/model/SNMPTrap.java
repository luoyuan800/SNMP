/*
 * SNMPTrap.java
 * Date: 4/27/2015
 * Time: 4:04 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.model;

import java.util.HashMap;
import java.util.Map;

public class SNMPTrap {
    private String community;
    private Map<String,String> values = new HashMap<String, String>();
    private String trapOid;
    private String address;

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getValue(String oid) {
        return values.get(oid);
    }

    public void setValues(String oid, String value) {
        this.values.put(oid, value);
    }

    public String getTrapOid() {
        return trapOid;
    }

    public void setTrapOid(String trapOid) {
        this.trapOid = trapOid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
