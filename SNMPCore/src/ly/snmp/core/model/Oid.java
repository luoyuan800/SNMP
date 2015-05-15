package ly.snmp.core.model;

import java.util.Arrays;

/**
 * OID model use for base data
 */
public class Oid {
    private int[] oids;
    private OIDType type;
    private String oidValue;
    private OIDValueType valueType;
    private Exception exception;
    private String oidString;
    private boolean isNext = false;

    /**
     * Create instance by use an array of int.
     * @param oids
     */
    public Oid(int[] oids) {
        this.oids = oids;
    }

    /**
     * Create Oid by giving oid.
     * @param oid
     */
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

    /**
     * Get this oid value
     * @return Value.toString()
     */
    public String getOidValue() {
        return oidValue;
    }

    public <T> T getValue() {
        return getValue(this.oidValue);
    }

    /**
     * Transform the value to special type by the oid value type
     * @param oidValue The value source input
     * @param <T> Special value type
     * @return The value which had been transform
     */
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

    /**
     * Get oid
     * @return Split into array as {1.3.4.5.7}
     */
    public int[] getOids() {
        return oids;
    }

    /**
     *
     * @return oid int array as {1.3.4.5.7.0}
     */
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

    /**
     * OID value type
     * @return count32, Integer32,...
     */
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

    /**
     * Is this oid not match the expect oid(use getNext)
     * @return
     */
    public boolean isNext() {
        return isNext;
    }

    /**
     * The oid type
     * @return private, MIB2...
     */
    public OIDType getType() {
        return type;
    }
}