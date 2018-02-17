package com.charlie.vm;

import com.charlie.common.SystemProperty;
import com.charlie.util.RefectUtil;
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
        loadAgent.invoke(vm_instance,jar);
    }
    public void detach() throws Exception{
        Method loadAgent = RefectUtil.getExeableMethod(vmClass, "detach");
        loadAgent.invoke(vm_instance);
        logger.info("has detach vm:{}",vm_instance);
    }
    private VirtualMachine(){

    }

    private static void initVM() throws Exception {
        loadTools();
        vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
    }
    private static void loadTools()throws Exception{
        String toolsPath = SystemProperty.TOOLS_PATH;
        URL url = new File(toolsPath).toURI().toURL();
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
        addURL.setAccessible(true);
        URLClassLoader classloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        addURL.invoke(classloader, new Object[] { url });
    }
    private static Object getTargetVM(String vmId) throws Exception {
        Object vm = RefectUtil.invokeStaticMethod("com.sun.tools.attach.VirtualMachine","attach", vmId);
        logger.info("vm is {}" ,vm.getClass());
        return  vm ;
    }
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    private static String getVMId(String vmId) throws Exception {
        if (isInteger(vmId)) {
            return vmId;
        }
        Method listMethod = RefectUtil.getExeableMethod(vmClass, "list");
        List<Object> vmList = (List<Object>)listMethod.invoke(null);
        for (Object virtualMachineDescriptor : vmList) {
            Method displayName = virtualMachineDescriptor.getClass().getDeclaredMethod("displayName");
            displayName.setAccessible(true);
            String display = (String)displayName.invoke(virtualMachineDescriptor);
            boolean contains = display.toUpperCase().contains(vmId.toUpperCase());
            if (contains) {
                Method idMethod = virtualMachineDescriptor.getClass().getDeclaredMethod("id");
                idMethod.setAccessible(true);
                String id = (String)idMethod.invoke(virtualMachineDescriptor);
                return id;
            }
        }
        return null;
    }
}
