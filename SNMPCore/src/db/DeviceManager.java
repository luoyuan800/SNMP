package db;

import ly.snmp.core.model.DeviceImp;

import java.util.List;

/**
 * Created by gluo on 11/7/2016.
 */
public class DeviceManager extends ly.snmp.core.service.DeviceManager {
    @Override
    public DeviceImp getDeviceByName(String name) {
        String sql = "select * from device where device_name = name";
        return null;
    }

    @Override
    public DeviceImp getDeviceByIP(String ip) {
        String sql = "select * from device where device_ip = ip";
        return null;
    }

    @Override
    public List<DeviceImp> getAllDevices() {
        return null;
    }

    @Override
    public List<String> getAllDeviceNames() {
        String sql = "select device_name from device";
        return null;
    }

    @Override
    public DeviceImp buildDeviceHistoryData(DeviceImp device) {
        String sql = "select * from oid_value JOIN oid ON oid_value.oid_id = oid.oid_id where oid_value.device_id = '" + device.getId() + "'";
        return null;
    }
}
