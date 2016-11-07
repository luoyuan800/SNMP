package ly.snmp.core.service;

import ly.snmp.core.model.DeviceImp;
import ly.snmp.core.model.SNMPParameter;
import ly.snmp.core.model.SNMPVersion;

import java.io.IOException;
import java.util.List;

/**
 * Created by gluo on 11/7/2016.
 */
public abstract class DeviceManager {
    public abstract DeviceImp getDeviceByName(String name);
    public abstract DeviceImp getDeviceByIP(String ip);
    public abstract List<DeviceImp> getAllDevices();
    public abstract List<String> getAllDeviceNames();
    public abstract DeviceImp buildDeviceHistoryData(DeviceImp device);
    public DeviceImp discoveryDevice(String ip, String community, SNMPVersion version){
        SNMPParameter parameter = new SNMPParameter();
        parameter.setCommunity(community);
        parameter.setVersion(version);
        DeviceImp device = new DeviceImp(ip);
        try {
            device.initDevice(parameter);
            device.discovery();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return device;
    }

    public List<DeviceImp> discoveryDevicesByIPRange(){
        return null;
    }
}
