package ly.snmp.core.service;

import ly.snmp.core.model.DataSet;
import ly.snmp.core.model.Device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SNMPManager implements Runnable {
    private static final SNMPManager _instance = new SNMPManager();
    private List<Device> deviceList = new ArrayList<Device>();
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> future;
    private Map<String, Future<?>> runningDevices = new HashMap<String, Future<?>>();

    public static SNMPManager getInstance() {
        return _instance;
    }

    private SNMPManager() {
        //TODO
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            scheduledExecutorService = Executors.newScheduledThreadPool(51);
        }
        this.future = scheduledExecutorService.scheduleWithFixedDelay(this, 5 * 60 * 1000, 10 * 5 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    private void managerDataSet() {
        //TODO Manager DataSet instances
        Set<DataSet> dataSets = DataSet.getAllDataSetInstances();
        for (DataSet dataSet : dataSets) {
            dataSet.rollUp();
        }
    }

    public List<Device> getDevices() {
        return deviceList;
    }

    public int addDevice(Device device) {
        deviceList.add(device);
        return deviceList.size();
    }

    public int removeDevice(String ip) {
        Device device = null;
        for (Device dev : deviceList) {
            if (dev.getIp().equals(ip)) {
                device = dev;
                break;
            }
        }
        if(device!=null) {
            Future running = runningDevices.get(device.getIp());
            if(running!=null && !running.isCancelled() && !running.isDone()) running.cancel(true);
            runningDevices.remove(device.getIp());
            deviceList.remove(device);
        }
        return deviceList.size();
    }

    @Override
    public void run() {
        for (Device device : deviceList) {
            runningDevices.put(device.getIp(), scheduledExecutorService.submit(device));
        }
    }

    public void destroy() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }
    }
}

