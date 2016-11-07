package ly.snmp.core.monitor;

import ly.snmp.core.model.OIDImp;
import ly.snmp.core.model.Oid;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gluo on 11/7/2016.
 */
public class Group implements Monitor {
    private String name;
    private String uuid;
    private Set<OIDImp> oids;
    @Override
    public Set<Oid> getOIDs() {
        HashSet<Oid> set = new HashSet<Oid>(oids);
        return set;
    }

    public void addOID(OIDImp oid){
        if(oids == null){
            oids = new HashSet<OIDImp>();
        }
        oids.add(oid);
    }

    @Override
    public void build(Long time) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
