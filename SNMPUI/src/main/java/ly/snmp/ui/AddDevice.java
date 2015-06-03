/*
 * AddDevice.java
 * Date: 6/1/2015
 * Time: 10:00 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.ui;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import ly.snmp.core.model.Device;
import ly.snmp.core.model.SNMPParameter;
import ly.snmp.core.model.SNMPVersion;
import ly.snmp.core.service.SNMPManager;

public class AddDevice extends HttpServlet{

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Start adding device!");
        String ip = req.getParameter("ip");
        Device device = new Device(ip);
        SNMPParameter parameter = new SNMPParameter();
        String version = req.getParameter("version");
        for(SNMPVersion ver : SNMPVersion.values()){
            if(ver.name().equals(version)){
                parameter.setVersion(ver);
            }
        }
        parameter.setCommunity(req.getParameter("community"));
        device.initDevice(parameter);
        System.out.println("Monitors : " + req.getParameter("monitors"));
        SNMPManager.getInstance().addDevice(device);
        resp.sendRedirect("/SNMPUI/jsp/devices.jsp");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println(req.getAuthType());
        PrintWriter print = resp.getWriter();
        print.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        print.println("<HTML>");
        print.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        print.println("  <BODY>");
        print.print("<a href=\"./jsp/addDevice.jsp\" name=\"Test\">Add Device</a>");
        print.println("  </BODY>");
        print.println("</HTML>");
        print.flush();
        print.close();
    }
}
