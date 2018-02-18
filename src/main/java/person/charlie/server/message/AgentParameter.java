package person.charlie.server.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlie on 2018/2/9.
 * Function:
 */
public class AgentParameter {
    private String vmId;
    private Map<String,byte[]> targetClasses = new HashMap<String, byte[]>(16);

    public AgentParameter(String vmId) {
        this.vmId = vmId;
    }

    public String getVmId() {
        return vmId;
    }

    public void setVmId(String vmId) {
        this.vmId = vmId;
    }

    public Map<String, byte[]> getTargetClasses() {
        return targetClasses;
    }

    public void setTargetClasses(Map<String, byte[]> targetClasses) {
        this.targetClasses = targetClasses;
    }
}
