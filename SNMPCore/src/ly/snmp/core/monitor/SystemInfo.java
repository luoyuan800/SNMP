/*
 * SystemInfo.java
 * Date: 4/3/2015
 * Time: 3:59 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.monitor;

import ly.snmp.core.model.Oid;
import ly.snmp.core.tool.SNMPUtilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Base system info for device<b>
 *     name, description, time, contanct, location, sevices, object id
 * </b>
 */
public class SystemInfo implements Monitor {
    public static final String
            SYSTEM_NAME = "1.3.6.1.2.1.1.5",
            SYSTEM_DESC = "1.3.6.1.2.1.1.1",
            SYSTEM_UP_TIME = "1.3.6.1.2.1.1.3",
            SYSTEM_CONTACT = "1.3.6.1.2.1.1.4",
            SYSTEM_LOCATION = "1.3.6.1.2.1.1.6",
            SYSTEM_SERVICES = "1.3.6.1.2.1.1.7",
            SYSTEM_OBJECT_ID = "1.3.6.1.2.1.1.2",
            SYSTEM_DATE = "1.3.6.1.2.1.25.1.2";
    private Set<Oid> oids;
    private Map<String, Oid> map;
    private Long lastUpdateTime;

    public SystemInfo() {
        oids = new HashSet<Oid>(7);
        map = new HashMap<String, Oid>(7);
        oids.addAll(Arrays.asList(SNMPUtilities.buildOid(SYSTEM_DESC,SYSTEM_CONTACT,SYSTEM_DATE,SYSTEM_LOCATION,SYSTEM_NAME,SYSTEM_OBJECT_ID,SYSTEM_SERVICES,SYSTEM_UP_TIME)));
        for (Oid oid : oids) {
            map.put(oid.getOidString(), oid);
        }
    }

    @Override
    public Set<Oid> getOIDs() {
        return oids;
    }

    @Override
    public void build(Long time) {
        this.lastUpdateTime = time;
    }

    public String get(String type) {
        try {
            return map.get(type).getValue();
        } catch (Exception e) {
            return null;
        }
    }

    public Oid getOid(String type) {
        return map.get(type);
    }


    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }
}
