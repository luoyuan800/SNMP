/*
 * MessageDispatcherLy.java
 * Date: 4/27/2015
 * Time: 11:10 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.snmputil.lysnmp;

import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.TransportMapping;
import org.snmp4j.TransportStateReference;
import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.event.CounterEvent;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.smi.Address;

import java.io.IOException;

public class MessageDispatcherLy extends MessageDispatcherImpl {
    public void processMessage(TransportMapping sourceTransport, Address incomingAddress, BERInputStream wholeMessage, TransportStateReference tmStateReference) {
        fireIncrementCounter(new CounterEvent(this, org.snmp4j.mp.SnmpConstants.snmpInPkts));
        if (!wholeMessage.markSupported()) {
            String txt = "Message stream must support marks";
            throw new IllegalArgumentException(txt);
        }
        try {
            wholeMessage.mark(16);
            BER.MutableByte type = new BER.MutableByte();
            // decode header but do not check length here, because we do only decode
            // the first 16 bytes.
            BER.decodeHeader(wholeMessage, type, false);
            if (type.getValue() != BER.SEQUENCE) {
                CounterEvent event = new CounterEvent(this,
                        org.snmp4j.mp.SnmpConstants.snmpInASNParseErrs);
                fireIncrementCounter(event);
                throw new RuntimeException("ASN.1 parse error (message is not a sequence)");
            }
            Integer64 version = new Integer64();
            version.decodeBER(wholeMessage);
            MessageProcessingModel mp = getMessageProcessingModel(version.toInt());
            if (mp == null) {
                CounterEvent event = new CounterEvent(this,
                        org.snmp4j.mp.SnmpConstants.snmpInBadVersions);
                fireIncrementCounter(event);
                throw new IllegalArgumentException("SNMP version " + version + " is not supported");
            } else {
                // reset it
                wholeMessage.reset();
                // dispatch it
                dispatchMessage(sourceTransport, mp, incomingAddress, wholeMessage, tmStateReference);
            }
        } catch (IOException iox) {
            CounterEvent event =
                    new CounterEvent(this, org.snmp4j.mp.SnmpConstants.snmpInvalidMsgs);
            fireIncrementCounter(event);
            throw new RuntimeException(String.format("Process message from address{%s} occurred exception.", incomingAddress.toString()), iox);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Process message from address{%s} occurred exception.", incomingAddress.toString()), ex);
        } catch (OutOfMemoryError oex) {
            throw new RuntimeException(String.format("Process message from address{%s} occurred exception.", incomingAddress.toString()), oex);
        }
    }
}
