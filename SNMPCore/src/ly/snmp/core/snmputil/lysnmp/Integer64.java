/*
 * Integert64.java
 * Date: 4/27/2015
 * Time: 11:34 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.snmputil.lysnmp;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.smi.Counter64;

import java.io.IOException;

/**
 * Default SNMP4j did not support the Integer64, and if a number is larger than integer32, it will not throw exception and return null
 * <br>
 * Here I build a new Integer64, and use it to replace the Integer34
 */
public class Integer64 extends Counter64 {

    @Override
    public void decodeBER(BERInputStream inputStream) throws java.io.IOException {
        BER.MutableByte type = new BER.MutableByte();
        long newValue = BER.decodeUnsignedInt64(inputStream, type);
        if (type.getValue() != BER.INTEGER) {
            throw new IOException("Wrong type encountered when decoding Counter: " + type.getValue());
        }
        setValue(newValue);
    }

}
