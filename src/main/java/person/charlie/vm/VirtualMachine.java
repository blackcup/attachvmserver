package person.charlie.vm;

import person.charlie.common.SystemProperty;
import person.charlie.util.RefectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.regex.Pattern;

public class VirtualMachine {
    private  static final Logger logger = LoggerFactory.getLogger(VirtualMachine.class);
    private static  Class<?> vmClass = null;
    private Object vm_instance = null;

    static {
        try {
            initVM();
        } catch (Exception e) {
            logger.error("something goes wrong ",e);
            System.exit(1);
        }
    }
    public static VirtualMachine attach(String vmId) throws Exception {
        String realId = getVMId(vmId);
        Object targetVM = getTargetVM(realId);
        VirtualMachine virtualMachine = new VirtualMachine();
        virtualMachine.vm_instance = targetVM;
        return virtualMachine;
    }
    public  void loadAgent(String jar) throws Exception {
        logger.info("agentPath:{}",jar);
        Class<?> vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
        Method loadAgent = RefectUtil.getExeableMethod(vmClass, "loadAgent", String.class);
        assert loadAgent != null;
        loadAgent.invoke(vm_instance,jar);
    }
    public void detach() throws Exception{
        Method loadAgent = RefectUtil.getExeableMethod(vmClass, "detach");
        assert loadAgent != null;
        loadAgent.invoke(vm_instance);
        logger.info("has detach vm:{}",vm_instance);
    }
    private VirtualMachine(){

    }

    private static void initVM() throws Exception {
        loadTools();
        vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
    }

    /***
     * load tools.jar by reflect
     * @throws Exception
     */
    private static void loadTools()throws Exception{
        String toolsPath = SystemProperty.TOOLS_PATH;
        URL url = new File(toolsPath).toURI().toURL();
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        URLClassLoader classloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        addURL.invoke(classloader, url);
    }

    private static Object getTargetVM(String vmId) {
        Object vm = RefectUtil.invokeStaticMethod("com.sun.tools.attach.VirtualMachine","attach", vmId);
        assert vm != null;
        logger.info("vm is {}" ,vm.getClass());
        return  vm ;
    }
    private  static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static String getVMId(String vmId) throws Exception {
        if (isInteger(vmId)) {
            return vmId;
        }
        Method listMethod = RefectUtil.getExeableMethod(vmClass, "list");
        assert listMethod != null ;
        List<?> vmList = (List<?>)listMethod.invoke(null);
        Class<?> vmdClass = Class.forName("com.sun.tools.attach.VirtualMachineDescriptor");
        for (Object virtualMachineDescriptor : vmList) {
            Method displayName = vmdClass.getDeclaredMethod("displayName");
            displayName.setAccessible(true);
            String display = (String)displayName.invoke(virtualMachineDescriptor);
            boolean contains = display.toUpperCase().contains(vmId.toUpperCase());
            if (contains) {
                Method idMethod = vmdClass.getDeclaredMethod("id");
                idMethod.setAccessible(true);
                return (String)idMethod.invoke(virtualMachineDescriptor);
            }
        }
        return null;
    }
}
