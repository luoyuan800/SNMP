package ly.snmp.core.model;

/**
 * Created by gluo on 11/7/2016.
 */
public class DeviceImp extends Device {
    private final static String SYSOBJECTID = "1.3.6.1.2.1.1.2";
    private String sysId;
    private String id;

    public DeviceImp(String ip) {
        super(ip);
    }

    public void discovery(){
        sysId = null;
        Oid sys = snmp.get(new Oid(SYSOBJECTID));
        sysId = sys.getOidValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
