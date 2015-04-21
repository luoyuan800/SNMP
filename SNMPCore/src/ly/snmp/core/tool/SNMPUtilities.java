/*
 * SNMPUtilities.java
 * Date: 4/7/2015
 * Time: 8:59 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.tool;

import ly.snmp.core.model.Oid;

import java.util.StringTokenizer;

public class SNMPUtilities {
    public static Oid[] buildOid(String... oids) {
        Oid[] oidArray = new Oid[oids.length];
        for (int i = 0; i < oids.length; i++) {
            oidArray[i] = new Oid(oids[i]);
        }
        return oidArray;
    }

    public static String stringFormat(String string) {
        try {
            StringTokenizer st = new StringTokenizer(string, ":");
            byte[] value = new byte[st.countTokens()];
            for (int n = 0; st.hasMoreTokens(); n++) {
                String s = st.nextToken();
                int code = Integer.parseInt(s, 16);
                value[n] = intAsByte(code);
            }
            return new String(value);
        } catch (Exception e) {
            //If an exception is throw, it means that this is not Hex String, just return the original is OK!
            return string;
        }
    }

    public static int replaceControlCode(int decCode) {
        ASCIICode asciiCode = ASCIICode.getASCII(decCode);
        if (asciiCode != ASCIICode.NO_CONTROL) {
            return ASCIICode.SPACE.getDecimal_Code();
        } else {
            return decCode;
        }
    }

    public static byte intAsByte(int value) {
        return (byte) (value & 0xff);
    }

    public static String convertHexStringToDateAndTime(String hexString) {
        byte[] bts = stringFormat(hexString).getBytes();
        byte[] format_str = toASCIICode(bts);
        return new String(format_str).trim();
    }

    public static byte[] toASCIICode(byte[] bts) {
        byte[] format_str = new byte[128];
        int btsLength = bts.length;
        int year = btsLength > 1 ? bts[0] * 256 + 256 + bts[1] : 1900;
        int month = btsLength > 2 ? bts[2] : 1;
        int day = btsLength > 3 ? bts[3] : 1;
        int hour = btsLength > 4 ? bts[4] : 0;
        int minute = btsLength > 5 ? bts[5] : 0;
        int second = btsLength > 6 ? bts[6] : 0;

        int index = 3;
        int temp = year;
        for (; index >= 0; index--) {
            format_str[index] = (byte) (48 + (temp - temp / 10 * 10));
            temp /= 10;
        }
        format_str[4] = '-';
        index = 6;
        temp = month;
        for (; index >= 5; index--) {
            format_str[index] = (byte) (48 + (temp - temp / 10 * 10));
            temp /= 10;
        }
        format_str[7] = '-';
        index = 9;
        temp = day;
        for (; index >= 8; index--) {
            format_str[index] = (byte) (48 + (temp - temp / 10 * 10));
            temp /= 10;
        }
        format_str[10] = ',';
        index = 12;
        temp = hour;
        for (; index >= 11; index--) {
            format_str[index] = (byte) (48 + (temp - temp / 10 * 10));
            temp /= 10;
        }
        format_str[13] = ':';
        index = 15;
        temp = minute;
        for (; index >= 14; index--) {
            format_str[index] = (byte) (48 + (temp - temp / 10 * 10));
            temp /= 10;
        }
        format_str[16] = ':';
        index = 18;
        temp = second;
        for (; index >= 17; index--) {
            format_str[index] = (byte) (48 + (temp - temp / 10 * 10));
            temp /= 10;
        }
        return format_str;
    }
}
