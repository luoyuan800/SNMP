package ly.snmp.core.model;

import java.util.Arrays;

public class Oid {
    private int[] oids;
    private OIDType type;
    private String oidValue;
    private OIDValueType valueType;
    private Exception exception;
    private String oidString;
    private boolean isNext = false;

    public Oid(int[] oids) {
        this.oids = oids;
    }

    public Oid(String oid) {
        if (oid == null || !oid.matches("(\\d\\.*)+")) {
            oid = "0.0";
            type = OIDType.ERROR;
        }
        type = OIDType.getType(oid);
        this.oidString = oid.trim();
        String[] strings = oidString.split("\\.");
        this.oids = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            this.oids[i] = Integer.parseInt(strings[i]);
        }
    }

    public String getOidValue() {
        return oidValue;
    }

    public <T> T getValue() {
        return getValue(this.oidValue);
    }

    protected <T> T getValue(String oidValue) {
        try {
            switch (valueType) {
                case Counter32:
                case Counter64:
                case INTEGER:
                case Gauge32:
                case Gauge64:
                    return (T) (Double.valueOf(oidValue));
                case TimeTicks:
                    return (T) (Long.valueOf(oidValue));
                default:
                    return (T) oidValue;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public int[] getOids() {
        return oids;
    }

    public int[] getOidsWithZero() {
        int[] ints = new int[oids.length];
        for (int i = 0; i < oids.length; i++) {
            ints[i] = oids[i];
        }
        ints[ints.length - 1] = 0;
        return ints;
    }

    public void setOidValue(String oidValue) {
        this.oidValue = oidValue;
    }

    public OIDValueType getValueType() {
        return valueType;
    }

    public void setValueType(OIDValueType valueType) {
        this.valueType = valueType;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String toString() {
        return (isNext ? "[Next]" : "") + oidString + "[" + valueType + "] = " + oidValue;
    }

    public void setOids(int[] oids) {
        this.oids = oids;
        this.oidString = Arrays.toString(oids).replaceAll(", ", ".").replace("[", "").replace("]", "");
    }

    public String getOidString() {
        return oidString;
    }

    public void setNext(boolean isNext) {
        this.isNext = isNext;
    }

    public boolean isNext() {
        return isNext;
    }

    public OIDType getType() {
        return type;
    }
}