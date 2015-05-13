/*
 * SNMP4j.java
 * Date: 4/1/2015
 * Time: 8:42 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.snmputil;

import ly.snmp.core.model.Device;
import ly.snmp.core.model.OIDValueType;
import ly.snmp.core.model.Oid;
import ly.snmp.core.model.SNMPParameter;
import ly.snmp.core.model.SNMPVersion;
import ly.snmp.core.model.TableColumnOid;
import ly.snmp.core.model.TableOid;
import ly.snmp.core.snmputil.lysnmp.MessageDispatcherLy;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SNMP4J implements SNMP {
    private SNMPParameter parameter;
    private Snmp snmp;
    private Target target;
    private PDUFactory pduFactory;
    private TableUtils tableUtils;

    public SNMP4J(SNMPParameter parameter) throws IOException {
        this.parameter = parameter;
        TransportMapping transport = new DefaultUdpTransportMapping();
        SecurityProtocols.getInstance().addDefaultProtocols();
        MessageDispatcher disp = new MessageDispatcherLy();
        switch (parameter.getVersion()) {
            case V1:
                disp.addMessageProcessingModel(new MPv1());
                break;
            case V2C:
                disp.addMessageProcessingModel(new MPv2c());
                break;
            case V3:
                disp.addMessageProcessingModel(new MPv3());
        }
        snmp = new Snmp(disp, transport);
        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
        USM usm = new USM(SecurityProtocols.getInstance(), localEngineID, 0);
        disp.addMessageProcessingModel(new MPv3(usm));
        transport.listen();
        this.target = createTarget();
        pduFactory = new DefaultPDUFactory();
        tableUtils = new TableUtils(snmp, pduFactory);
    }

    public Target createTarget() throws UnknownHostException {
        switch (parameter.getVersion()) {
            case V1:
            case V2C:
                CommunityTarget target = new CommunityTarget();
                Address targetAddress = new UdpAddress(InetAddress.getByName(parameter.getIp()), parameter.getPort());
                target.setCommunity(new OctetString(parameter.getCommunity()));
                target.setAddress(targetAddress);
                target.setRetries(3);
                target.setTimeout(3000);
                if (parameter.getVersion() == SNMPVersion.V1) {
                    target.setVersion(SnmpConstants.version1);
                } else {
                    target.setVersion(SnmpConstants.version2c);
                }
                return target;
            case V3:
                //TODO
                throw new UnsupportedOperationException("Not support V3 currently!");
        }
        return null;
    }

    @Override
    public Oid get(Oid oid) {
        PDU pdu = pduFactory.createPDU(target);
        OID oid4J = new OID(oid.getOids());
        pdu.add(new VariableBinding(oid4J));
        try {
            ResponseEvent response = snmp.getNext(pdu, target);
            for (VariableBinding vb : response.getResponse().toArray()) {
                Variable variable = vb.getVariable();
                if (vb.getOid().startsWith(oid4J)) {
                    setValueAndType(oid, variable);
                } else {
                    oid.setValueType(OIDValueType.ERROR);
                }
            }
        } catch (IOException e) {
            oid.setValueType(OIDValueType.ERROR);
            oid.setException(e);
        }
        return oid;
    }


    @Override
    public Oid walk(Oid oid) {
        throw new UnsupportedOperationException("UnSupport this operation in current version!");
    }

    @Override
    public Oid getNext(Oid oid) {
        PDU pdu = pduFactory.createPDU(target);
        OID oid4J = new OID(oid.getOids());
        pdu.add(new VariableBinding(oid4J));
        try {
            ResponseEvent response = snmp.getNext(pdu, target);
            for (VariableBinding vb : response.getResponse().toArray()) {
                Variable variable = vb.getVariable();
                setValueAndType(oid, variable);
                if (!vb.getOid().startsWith(oid4J)) {
                    oid.setOids(vb.getOid().getValue());
                    oid.setNext(true);
                }
            }
        } catch (IOException e) {
            oid.setValueType(OIDValueType.ERROR);
            oid.setException(e);
        }
        return oid;
    }

    @Override
    public TableOid getTable(TableOid table) {
        Map<String, TableColumnOid> columnMap = new HashMap<String, TableColumnOid>(table.getColumns().length);
        OID[] columns = new OID[table.getColumns().length];
        for(int i=0;i<columns.length;i++){
            columns[i] = new OID(table.getColumns()[i].getOids());
            columnMap.put(table.getColumns()[i].getOidString(), table.getColumns()[i]);
        }
        List<TableEvent> tableEvents = tableUtils.getTable(target, columns, null, null);
        for(TableEvent tableEvent : tableEvents){
            if(tableEvent == null){
                continue;
            }
            OID index = tableEvent.getIndex();
            for(VariableBinding  variableBinding : tableEvent.getColumns()){
                if(variableBinding==null){
                    continue;
                }
                OID oid = variableBinding.getOid();
                oid.trim(index.size());
                TableColumnOid column = columnMap.get(oid.toDottedString());
                Variable variable = variableBinding.getVariable();
                if(variable!=null && !variable.isException()){
                    setTableColumnValueType(index, column, variable);
                }else{
                    column.setValueType(OIDValueType.ERROR);
                }
            }
        }
        return table;
    }

    @Override
    public Oid[] get(Oid... oids) {
        PDU pdu = pduFactory.createPDU(target);
        List<OID> oidList = new ArrayList<OID>(oids.length);
        for (Oid oid : oids) {
            if(oid instanceof TableOid){
                this.getTable((TableOid) oid);
            }else {
                OID oid4j = new OID(oid.getOids());
                pdu.add(new VariableBinding(oid4j));
                oidList.add(oid4j);
            }
        }
        try {
            ResponseEvent responseEvent = snmp.getNext(pdu, target);
            PDU response = responseEvent.getResponse();
            for (int i = 0; i < response.size(); i++) {
                VariableBinding variableBinding = response.get(i);
                Variable variable = variableBinding.getVariable();
                if (variableBinding.getOid().startsWith(oidList.get(i))) {
                    setValueAndType(oids[i], variable);
                } else {
                    oids[i].setValueType(OIDValueType.ERROR);
                }
            }
        } catch (IOException e) {
            for (Oid oid : oids) {
                oid.setException(e);
                oid.setValueType(OIDValueType.ERROR);
            }
        }
        return oids;
    }

    @Override
    public Oid[] getNext(Oid... oids) {
        PDU pdu = pduFactory.createPDU(target);
        List<OID> oidList = new ArrayList<OID>(oids.length);
        for (Oid oid : oids) {
            OID oid4j = new OID(oid.getOids());
            pdu.add(new VariableBinding(oid4j));
            oidList.add(oid4j);
        }
        try {
            ResponseEvent responseEvent = snmp.getNext(pdu, target);
            PDU response = responseEvent.getResponse();
            for (int i = 0; i < response.size(); i++) {
                VariableBinding variableBinding = response.get(i);
                Variable variable = variableBinding.getVariable();
                if (variableBinding.getOid().startsWith(oidList.get(i))) {
                    oids[i].setNext(true);
                    oids[i].setOids(variableBinding.getOid().getValue());
                }
                setValueAndType(oids[i], variable);
            }
        } catch (IOException e) {
            for (Oid oid : oids) {
                oid.setException(e);
                oid.setValueType(OIDValueType.ERROR);
            }
        }
        return oids;
    }

    private void setValueAndType(Oid oid, Variable variable) {
        switch (variable.getSyntax()) {
            case SMIConstants.SYNTAX_COUNTER64:
                oid.setValueType(OIDValueType.Counter64);
                break;
            case SMIConstants.SYNTAX_COUNTER32:
                oid.setValueType(OIDValueType.Counter32);
                break;
            case SMIConstants.SYNTAX_GAUGE32:
                oid.setValueType(OIDValueType.Gauge32);
                break;
            case SMIConstants.SYNTAX_INTEGER:
                oid.setValueType(OIDValueType.INTEGER);
                break;
            case SMIConstants.SYNTAX_IPADDRESS:
                oid.setValueType(OIDValueType.NetworkAddress);
                break;
            case SMIConstants.SYNTAX_TIMETICKS:
                oid.setValueType(OIDValueType.TimeTicks);
                break;
            case SMIConstants.SYNTAX_OCTET_STRING:
                oid.setValueType(OIDValueType.String);
                oid.setOidValue(((OctetString)variable).toASCII(' '));
                return;
            default:
                oid.setValueType(OIDValueType.String);
        }
        oid.setOidValue(variable.toString());
    }
    private void setTableColumnValueType(OID index, TableColumnOid oid, Variable variable) {
        switch (variable.getSyntax()) {
            case SMIConstants.SYNTAX_COUNTER64:
                oid.setValueType(OIDValueType.Counter64);
                break;
            case SMIConstants.SYNTAX_COUNTER32:
                oid.setValueType(OIDValueType.Counter32);
                break;
            case SMIConstants.SYNTAX_GAUGE32:
                oid.setValueType(OIDValueType.Gauge32);
                break;
            case SMIConstants.SYNTAX_INTEGER:
                oid.setValueType(OIDValueType.INTEGER);
                break;
            case SMIConstants.SYNTAX_IPADDRESS:
                oid.setValueType(OIDValueType.NetworkAddress);
                break;
            case SMIConstants.SYNTAX_TIMETICKS:
                oid.setValueType(OIDValueType.TimeTicks);
                oid.setOidValue(index.toDottedString(),((TimeTicks)variable).toMilliseconds() + "");
                return;
            case SMIConstants.SYNTAX_OCTET_STRING:
                oid.setValueType(OIDValueType.String);
                oid.setOidValue(index.toDottedString(), ((OctetString)variable).toASCII(' '));
                return;
            default:
                oid.setValueType(OIDValueType.String);
        }
        oid.setOidValue(index.toString(), variable.toString());
    }
}
