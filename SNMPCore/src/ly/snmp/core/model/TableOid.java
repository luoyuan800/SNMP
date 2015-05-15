/*
 * TableOID.java
 * Date: 3/31/2015
 * Time: 8:44 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.model;

/**
 * TableOid is different from {@link Oid}<br>
 * Those value in TableOid should be list.
 */
public class TableOid extends Oid {
    private TableColumnOid[] columns;

    public TableOid(String table, String... columns) {
        super(table);
        this.columns = new TableColumnOid[columns.length];
        for (int i = 0; i < columns.length; i++) {
            this.columns[i] = new TableColumnOid(columns[i]);
        }
    }

    /**
     * This will throw exception because the table have many column and did not have one value as Oid
     * See {@link #getColumns()}
     * @return
     *
     */
    public String getOidValue() {
        throw new UnsupportedOperationException("This is a table, use getColumns to get the value");
    }

    /**
     * <p>
     * Table should have many column as<br>
     *          tableoid<br>
     *              tablecolumn<br>
     *              tablecolumn<br>
     *              ...<br>
     * About how to get table value should use those code like this:<br>
     *  for(TableColumnOid column : tableoid.getColumns()){<br>
     *      for(String index : column.getIndex()){<br>
     *          String value = column.getValue(index);<br>
     *      }<br>
     *   }<br>
     *   }<br>
     * Because about that the table column should have index for each row.
     * </p>
     * @return
     */
    public TableColumnOid[] getColumns() {
        return columns;
    }
}
