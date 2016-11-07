package ly.snmp.core.service;

import ly.snmp.core.model.Device;
import ly.snmp.core.monitor.Group;

import java.util.List;
import java.util.UUID;

/**
 * Created by gluo on 11/7/2016.
 */
public abstract class GroupManager {
    public abstract List<String> getAllGroupNames();
    public abstract Group getGroup(String name);
    public abstract List<String> getGroupNamesByDevice(Device device);
    public abstract void save(Group group);
    Group createGroup(String name){
        Group group = new Group();
        group.setName(name);
        group.setUuid(UUID.randomUUID().toString());
        return group;
    }
}
