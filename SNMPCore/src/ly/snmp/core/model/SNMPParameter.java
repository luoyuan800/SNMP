/*
 * SNMPParameter.java
 * Date: 4/27/2015
 * Time: 10:11 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.core.model;
/*
* The SNMP parameter bean class<br>
*     If you want to init the snmp util, you should set those snmp config,the default snmp port are 163
 */
public class SNMPParameter {
    private int port = 161;
    private SNMPVersion version;
    private String community;
    private int trapPort = 162;
    private String ip;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SNMPVersion getVersion() {
        return version;
    }

    public void setVersion(SNMPVersion version) {
        this.version = version;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public int getTrapPort() {
        return trapPort;
    }

    public void setTrapPort(int trapPort) {
        this.trapPort = trapPort;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
