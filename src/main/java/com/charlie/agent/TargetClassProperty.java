package com.charlie.agent;

/**
 * Created by chenc49 on 2018/2/8.
 * Function:
 */
public class TargetClassProperty {
    private String className;
    private String bytesPath;

    public String getClassName() {
        return className;
    }

    public TargetClassProperty() {
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public TargetClassProperty(String className, String bytesPath, String vmId) {
        this.className = className;
        this.bytesPath = bytesPath;
        this.vmId = vmId;
    }

    public String getBytesPath() {
        return bytesPath;
    }

    public void setBytesPath(String bytesPath) {
        this.bytesPath = bytesPath;
    }

    public String getVmId() {
        return vmId;
    }

    public void setVmId(String vmId) {
        this.vmId = vmId;
    }

    private String vmId;
}
