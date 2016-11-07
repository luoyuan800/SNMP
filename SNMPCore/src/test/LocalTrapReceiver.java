package test;

import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.security.UsmUserEntry;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import java.io.IOException;
import java.util.Vector;

/**
 * 本类用于监听发送到本机的Trap信息
 *
 * @author luoyuan
 */
public class LocalTrapReceiver implements CommandResponder {

    private MultiThreadedMessageDispatcher dispatcher;
    private Snmp snmp = null;

    private LocalTrapReceiver() throws IOException {
        dispatcher = new MultiThreadedMessageDispatcher(ThreadPool.create("snmp trap", 2),
                new MessageDispatcherImpl());
        Address listenUdpAddress = GenericAddress.parse("udp:0.0.0.0/162"); // udp监听端口
        Address listenTCPAddress = GenericAddress.parse("tcp:0.0.0.0/162"); // tcp监听端口
        TransportMapping transport;
        // 对TCP与UDP协议进行处理
        DefaultUdpTransportMapping udpTransport = new DefaultUdpTransportMapping(
                (UdpAddress) listenUdpAddress);
        DefaultTcpTransportMapping tcpTransport = new DefaultTcpTransportMapping(
                (TcpAddress) listenTCPAddress);

        snmp = new Snmp(dispatcher, udpTransport);
        snmp.addTransportMapping(tcpTransport);

        //配置engine
        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
        //配置USM 和user，需要和发送trap的配置一样才可以成功接收trap
        USM usm = new USM(SecurityProtocols.getInstance(), localEngineID, 0);
        usm.updateUser(new UsmUserEntry(new OctetString("123"), new UsmUser(new OctetString("123"),
                null,
                null,
                null,
                null)));
//        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3(usm));
        snmp.listen();
    }


    public void run() {
        try {
            snmp.addCommandResponder(this);
            System.out.println("开始监听Trap信息!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 当接收到trap时，会自动进入这个方法
     *
     * @param respEvnt
     */
    public void processPdu(CommandResponderEvent respEvnt) {
        // 解析Response并且直接打印出来
        if (respEvnt != null && respEvnt.getPDU() != null) {
            System.out.println("security name = " + new OctetString(respEvnt.getSecurityName()));
            PDU pdu = respEvnt.getPDU();
            System.out.println("PDU type = " + respEvnt.getPDU().getClass().getSimpleName());
            System.out.println("PDU = " + respEvnt.getPDU().toString());
            if(pdu instanceof ScopedPDU){
                ScopedPDU scopedPDU = (ScopedPDU) pdu;
                System.out.println("Engine Id = " + scopedPDU.getContextEngineID().getSyntaxString());
            }
            Vector<? extends VariableBinding> recVBs = respEvnt.getPDU().getVariableBindings();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                System.out.println(recVB.getOid() + " : " + recVB.getVariable());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        LocalTrapReceiver localTrapReceiver = new LocalTrapReceiver();
        localTrapReceiver.run();
    }

}