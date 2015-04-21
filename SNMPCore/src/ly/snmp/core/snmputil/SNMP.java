/*
 * SNMP.java
 * Date: 3/31/2015
 * Time: 8:39 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.snmputil;

import ly.snmp.core.model.Oid;
import ly.snmp.core.model.TableOid;

import java.io.IOException;

public interface SNMP {
    public Oid get(Oid oid);
    public Oid walk(Oid oid);
    public Oid getNext(Oid oid);
    public TableOid getTable(TableOid table);
    public Oid[] get(Oid...oids);
    public Oid[] getNext(Oid...oids);
}
