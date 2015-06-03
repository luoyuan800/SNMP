package ly.snmp.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ly.snmp.core.model.DataSet;
import ly.snmp.core.model.Device;
import ly.snmp.core.monitor.CPU;
import ly.snmp.core.service.SNMPManager;

public class GetDevices extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		StringBuilder sb = new StringBuilder("{");
		SNMPManager manager = SNMPManager.getInstance();
		List<Device> devices = manager.getDevices();
		sb.append("\"total\":").append(devices.size()).append(",").append("\"rows\":[");
		for(int i=0; i<devices.size(); i++){
			Device device = devices.get(i);
			sb.append("{").append("\"ip\":\"").append(device.getIp()).append("\",");
			CPU cpu = device.getMonitor(CPU.class);
			if(cpu!=null){
				DataSet<Double> utilization = cpu.getUtilization();
				
				sb.append("\"cpu\":\"").append(utilization).append("\",");
			}else{
				sb.append("\"cpu\":\"").append("tbd").append("\",");
			}
			sb.append("\"memory\":\"").append("tbd").append("\"");
			sb.append("}");
			if(i<devices.size()-1){
				sb.append(",");
			}
		}
		sb.append("]}");
		PrintWriter print = resp.getWriter();
		print.print(sb.toString());
		print.flush();
        print.close();
	}
}
