/*
 * SNMPTrapListener.java
 * Date: 4/27/2015
 * Time: 3:56 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.snmputil.trap;

import ly.snmp.core.model.SNMPTrap;
import ly.snmp.core.snmputil.lysnmp.MessageDispatcherLy;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SNMPTrapListener implements CommandResponder {
    private List<SNMPTrap> receivedTraps = new ArrayList<SNMPTrap>();
    private Snmp snmp;
    private TransportMapping udpTrap;
    private TransportMapping tcpTrap;

    public SNMPTrapListener(int port) throws IOException {
        udpTrap = new DefaultUdpTransportMapping(new UdpAddress("0.0.0.0/" + port));
        tcpTrap = new DefaultTcpTransportMapping(new TcpAddress("0.0.0.0/" + port));
        MessageDispatcherLy dispatcher = new MessageDispatcherLy();
        dispatcher.addMessageProcessingModel(new MPv1());
        dispatcher.addMessageProcessingModel(new MPv2c());
        dispatcher.addMessageProcessingModel(new MPv3());
        SecurityProtocols.getInstance().addDefaultProtocols();
        snmp = new Snmp(udpTrap);
        snmp.addCommandResponder(this);
        snmp.addTransportMapping(udpTrap);
        snmp.addTransportMapping(tcpTrap);
        snmp.listen();
        System.out.println("UDP Listen ? " + udpTrap.isListening());
        System.out.println("TCP Listen ? " + tcpTrap.isListening());
    }

    @Override
    public void processPdu(CommandResponderEvent event) {
        String community = new OctetString(event.getSecurityName()).toString();
        int securityLev = event.getSecurityLevel();
        PDU command = event.getPDU();
        if (command != null) {
            if (command instanceof PDUv1) {
                processv1Trap((PDUv1) command, community);
            } else {
                final TransportIpAddress ip = (TransportIpAddress) event.getPeerAddress();
                processv2Trap(command, ip.getInetAddress(), community, securityLev);
            }
        }
    }

    public void processv1Trap(final PDUv1 trapPDU, String community) {
        final String oidindex;

        if (trapPDU.getGenericTrap() != PDUv1.ENTERPRISE_SPECIFIC) {
            oidindex = "1.3.6.1.6.3.1.1.5." + (trapPDU.getGenericTrap() + 1);
        } else {
            oidindex = trapPDU.getEnterprise() + "." + trapPDU.getSpecificTrap();
        }

        final List<VariableBinding> varBinds = new ArrayList<VariableBinding>(trapPDU.getVariableBindings());
        handleTrap(oidindex, trapPDU.getAgentAddress().toString(), community, varBinds);
    }

    public void processv2Trap(final PDU trapPDU, final InetAddress inetAddress, String community, int securityLevel) {
        final List<VariableBinding> varBinds = new ArrayList<VariableBinding>(trapPDU.getVariableBindings());
        final List<VariableBinding> trapVariables = new ArrayList<VariableBinding>(varBinds.size());
        String trapOid = null;

        String ipAddress = inetAddress.getHostAddress();

        for (VariableBinding varBind : varBinds) {
            final String varBindOid = varBind.getOid().toString();
            if (varBindOid.startsWith("1.3.6.1.6.3.1.1.4.1") || varBindOid.startsWith("1.3.6.1.6.3.1.1.4.3")) { // SNMPv2-MIB::snmpTrapOID.0 or snmpTrapEnterprise
                trapOid = varBind.getVariable().toString();
            } else if (varBindOid.startsWith("1.3.6.1.6.3.18.1.3")) {
                // RFC3584 section 3.1.4, look for actual IP Address in VarBind section
                ipAddress = varBind.getVariable().toString();
            } else {
                trapVariables.add(varBind);
            }
        }
        if (trapOid == null) {
            if (varBinds.size() > 1) {
                trapOid = varBinds.get(1).getVariable().toString();
            } else {
                trapOid = "1.3.6.1.6.3.1.1.4.1.0";
            }
        }

        handleTrap(trapOid, ipAddress, community, trapVariables);
    }

    private void handleTrap(final String trapOid, final String sourceAddress, final String community, final List<VariableBinding> varBinds) {
        SNMPTrap trap = new SNMPTrap();
        trap.setCommunity(community);
        trap.setAddress(sourceAddress);
        trap.setTrapOid(trapOid);
        for (VariableBinding vb : varBinds) {
            trap.setValues(vb.getOid().toString(), vb.toValueString());
        }
        receivedTraps.add(trap);
    }

    public List<SNMPTrap> getReceivedTraps() {
        return Collections.unmodifiableList(receivedTraps);
    }

    public void stop() throws IOException {
        snmp.close();
    }
}
