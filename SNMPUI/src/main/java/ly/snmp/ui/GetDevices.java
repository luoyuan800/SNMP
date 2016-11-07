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
import ly.snmp.core.monitor.Memory;
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
				DataSet<Double> cpuutil = cpu.getUtilization();
				
				sb.append("\"cpu\":\"").append(cpuutil.getLatestData()!=null ? cpuutil.getLatestData() : "-").append("\",");
			}else{
				sb.append("\"cpu\":\"").append("Add Memory Monitor").append("\",");
			}
			Memory memory = device.getMonitor(Memory.class);
			if(memory!=null){
				DataSet<Double> memUse = memory.getUsed();
				DataSet<Double> menTol = memory.getTotalSize();
				if(memUse!=null&&menTol!=null){
					Double use = memUse.getLatestData();
					Double tol = menTol.getLatestData();
					if(use!=null&& tol!=null){
						sb.append("\"memory\":\"").append(use/tol).append("\"");
					}else{
						sb.append("\"memory\":\"").append("-").append("\"");
					}
				}else{
					sb.append("\"memory\":\"").append("-").append("\"");
				}
			}else{
				sb.append("\"memory\":\"").append("Add Memory Monitor").append("\"");
			}
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
