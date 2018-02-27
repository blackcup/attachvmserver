package person.charlie.agent;

/**
 * Created by charlie on 2018/2/8.
 * Function:
 */
public class TargetClass {
    private String className;
    private String bytesPath;

    public String getClassName() {
        return className;
    }

    public TargetClass() {
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "TargetClass{" +
                "className='" + className + '\'' +
                ", bytesPath='" + bytesPath + '\'' +
                ", vmId='" + vmId + '\'' +
                '}';
    }

    public TargetClass(String className, String bytesPath, String vmId) {
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
