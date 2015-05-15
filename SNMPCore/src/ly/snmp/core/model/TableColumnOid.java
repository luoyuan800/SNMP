/*
 * TableColumnOID.java
 * Date: 3/31/2015
 * Time: 8:58 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Table column oid will set as child in {@link TableOid}
 */
public class TableColumnOid extends Oid {
    private Map<String, String> oidValue;
    public TableColumnOid(String oid) {
        super(oid);
        oidValue = new HashMap<String, String>();
    }

    public void setOidValue(String value){
        throw new UnsupportedOperationException("This is a table column, use the setOidValue(index, value)!");
    }
    public void setOidValue(String index, String value){
        oidValue.put(index, value);
    }

    /**
     * See {@link TableOid#getColumns()}
     * @param index
     * @param <T>
     * @return
     */
    public <T> T getValue(String index){
        return super.getValue(oidValue.get(index));
    }

    /**
     * See {@link TableOid#getColumns()}
     * @return
     */
    public Set<String> getIndex(){
        return oidValue.keySet();
    }
}
