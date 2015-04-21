/*
 * OIDType.java
 * Date: 3/30/2015
 * Time: 9:17 AM
 *
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/
package ly.snmp.core.model;

public enum OIDType {
    MIB2("1.3.6.1.2.1"),
    PRIVATE("1.3.6.1.4"),
    UNKNOWN("0"),
    ERROR("0.0");
    private String oid;
    private OIDType(String oid){
        this.oid = oid;
    }
    public static OIDType getType(String oid){
        for(OIDType type : values()){
            if(oid.startsWith(type.oid)){
                return type;
            }
        }
        return UNKNOWN;
    }
}
