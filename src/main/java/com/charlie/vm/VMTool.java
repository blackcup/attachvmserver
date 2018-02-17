package com.charlie.vm;

import com.charlie.common.CommonConf;
import com.charlie.common.SystemProperty;
import com.charlie.util.RefectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by charlie on 2018/2/8.
 * Function:
 */
public class VMTool {

    public static final Logger logger = LoggerFactory.getLogger(CommonConf.class);
    public static void applyChange(String vmId) throws Exception {
        Object vm = getTargetVM(vmId);
        logger.info("has get attach on {},VM instance is {}",vmId,vm);
        attachAgent(vm);
        logger.info("has loaded the agent jar");
        Thread.sleep(1000);
        detachAgent(vm);
        logger.info("has detach the vm:{}",vm);
    }

    private static void detachAgent(Object vm) throws Exception {
        Class<?> vmClass = null;
        vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
        Method loadAgent = RefectUtil.getExeableMethod(vmClass, "detach");
        loadAgent.invoke(vm);
        logger.info("has detach vm:{}",vm);
    }

    private static void attachAgent(Object vm) throws Exception {
        String agentPath = locateAgentJar();
        logger.info("agentPath:{}",agentPath);
        Class<?> vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
        Method loadAgent = RefectUtil.getExeableMethod(vmClass, "loadAgent", String.class);
        loadAgent.invoke(vm,agentPath);
    }

    private static String locateAgentJar(){
        //current jar is agent jarn
        File file = new File("");
        String absolutePath = file.getAbsolutePath();
        logger.info("getAbsolutePath:{}",absolutePath);
        String property = System.getProperty("java.class.path");
        return absolutePath + SystemProperty.FILE_SEPARATOR + property;
    }

    private static Object getTargetVM(String vmId) throws Exception {
        String toolsPath = SystemProperty.TOOLS_PATH;
        URL url = new File(toolsPath).toURI().toURL();
        Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
        add.setAccessible(true);
        URLClassLoader classloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        add.invoke(classloader, new Object[] { url });
        Object vm = RefectUtil.invokeStaticMethod("com.sun.tools.attach.VirtualMachine","attach", vmId);
        logger.info("vm is {}" ,vm.getClass());
        return  vm ;
    }
    private static String getVMId(String vmId){
        return vmId;
    }

}
