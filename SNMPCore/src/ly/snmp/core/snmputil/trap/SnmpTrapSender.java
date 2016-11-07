package ly.snmp.core.snmputil.trap;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.security.UsmUserEntry;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Map;

/**
 * 本类用于发送v3 Trap信息
 *
 * @author luoyuan
 */
public class SnmpTrapSender {

    private Snmp snmp = null;

    private Address targetAddress = null;

    private PDU pdu;

    public SnmpTrapSender(String target, int port){

        // 设置目的地的IP和端口
        targetAddress = GenericAddress.parse(String.format("udp:%s/%s", target, port));


    }

    public void initSNMP(String userName, String securityName, OID acthModel, String authenticationPassphrase, OID privModel, String privacyPassphrase) throws IOException {
        //配置EngineID
        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
        TransportMapping transport = new DefaultUdpTransportMapping();
        //配置USM，传入 engine id
        USM usm = new USM(SecurityProtocols.getInstance(), localEngineID, 0);
        //添加Usm user， 这里的配置的user， 目标接收端也正确认证才可以接受到这个trap
        //这里创建User的是全部使用null是表示使用NOAUTH_NOPRIV的模式发送trap
        //具体的Auth模式可以参考收集snmp数据，是一样配置的。
        usm.updateUser(new UsmUserEntry(new OctetString(userName), new UsmUser(new OctetString(securityName),
                acthModel,
                new OctetString(authenticationPassphrase),
                privModel,
                new OctetString(privacyPassphrase))));
        //添加USM到Security Models中
        SecurityModels.getInstance().addSecurityModel(usm);
        //构建SNMP对象， 并且添加v3模式到其中
        snmp = new Snmp(transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3(usm));
        transport.listen();
    }

    public void initSNMP(String userName, String securityName) throws IOException {
        //配置EngineID
        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
        TransportMapping transport = new DefaultUdpTransportMapping();
        //配置USM，传入 engine id
        USM usm = new USM(SecurityProtocols.getInstance(), localEngineID, 0);
        //添加Usm user， 这里的配置的user， 目标接收端也正确认证才可以接受到这个trap
        //这里创建User的是全部使用null是表示使用NOAUTH_NOPRIV的模式发送trap
        //具体的Auth模式可以参考收集snmp数据，是一样配置的。
        usm.updateUser(new UsmUserEntry(new OctetString(userName), new UsmUser(new OctetString(securityName),
                AuthMD5.ID,
                null,
                Priv3DES.ID,
                null)));
        //添加USM到Security Models中
        SecurityModels.getInstance().addSecurityModel(usm);
        //构建SNMP对象， 并且添加v3模式到其中
        snmp = new Snmp(transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3(usm));
        transport.listen();
    }

    public void initSNMP(Snmp snmp){
        this.snmp = snmp;
    }

    /**
     * 向管理进程发送Trap报文
     *
     * @throws IOException
     */
    public void sendV3PDU() throws IOException {

        // 设置 target, v3trap 一定要使用UserTarget
        UserTarget target = new UserTarget();
        target.setAddress(targetAddress);
        target.setVersion(SnmpConstants.version3);
        //设置用户认证信息
        target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        target.setSecurityName(new OctetString("123"));
        target.setAuthoritativeEngineID(MPv3.createLocalEngineID());

        // 创建 PDU, 注意这里要使用ScopedPDU
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.4407.11.1.1.1.1"),
                new OctetString("SnmpTrap")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.4407.11.1.1.1.2"),
                new OctetString("v3")));
        pdu.setType(PDU.TRAP);

        // 发送trap
        snmp.send(pdu, target);
    }

    /**
     * 向管理进程发送Trap报文
     *
     * @throws IOException
     */
    public void sendV1PDU() throws IOException {

        CommunityTarget target = new CommunityTarget(targetAddress, new OctetString("luo"));
        target.setAddress(targetAddress);
        target.setVersion(SnmpConstants.version1);

        pdu.setType(PDU.V1TRAP);

        // 发送trap
        snmp.send(pdu, target);
    }

    public void setVariableBinding(Map<String, Object> varis){
        pdu = new ScopedPDU();
        for(Map.Entry<String, Object> entry : varis.entrySet()){
            if(entry.getValue() instanceof String){
                pdu.add(new VariableBinding(new OID(entry.getKey()),new OctetString(entry.getValue().toString())));
            }
        }
    }

    public static void main(String[] args) {
        try {
            SnmpTrapSender trapSender = new SnmpTrapSender("10.154.10.11", 162);
            trapSender.sendV3PDU();
//            trapSender.sendV1PDU();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}