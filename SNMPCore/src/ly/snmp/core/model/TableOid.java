/*
 * TableOID.java
 * Date: 3/31/2015
 * Time: 8:44 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.model;

public class TableOid extends Oid {
    private TableColumnOid[] columns;

    public TableOid(String table, String... columns) {
        super(table);
        this.columns = new TableColumnOid[columns.length];
        for (int i = 0; i < columns.length; i++) {
            this.columns[i] = new TableColumnOid(columns[i]);
        }
    }

    public String getOidValue() {
        throw new UnsupportedOperationException("This is a table, use getColumns to get the value");
    }

    public TableColumnOid[] getColumns() {
        return columns;
    }
}
