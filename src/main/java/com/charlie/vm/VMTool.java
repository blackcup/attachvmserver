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
 * Created by chenc49 on 2018/2/8.
 * Function:
 */
public class VMTool {

    public static final Logger logger = LoggerFactory.getLogger(CommonConf.class);
    public static void applyChange(String vmId){
        Object vm = getTargetVM(vmId);
        logger.info("has get attach on {},VM instance is {}",vmId,vm);
        attachAgent(vm);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // detachAgent(vm);
    }

    private static void detachAgent(Object vm) {
        Class<?> vmClass = null;
        try {
            vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
            Method loadAgent = RefectUtil.getExeableMethod(vmClass, "detach");
            loadAgent.invoke(vm);
            logger.info("has detach vm:{}",vm);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static void attachAgent(Object vm) {
        String agentPath = locateAgent();
        logger.info("agentPath:{}",agentPath);
        try {
            Class<?> vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
            Method loadAgent = RefectUtil.getExeableMethod(vmClass, "loadAgent", String.class);
            loadAgent.invoke(vm,agentPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static String locateAgent(){
        //current jar is agent jarn
        File file = new File("");
        String absolutePath = file.getAbsolutePath();
        logger.info("getAbsolutePath:{}",absolutePath);
        String property = System.getProperty("java.class.path");
        return absolutePath + SystemProperty.FILE_SEPARATOR + property;
    }

    private static Object getTargetVM(String vmId) {
        try {
            String toolsPath = SystemProperty.TOOLS_PATH;
            URL url = new File(toolsPath).toURI().toURL();
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            add.setAccessible(true);
            URLClassLoader classloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            add.invoke(classloader, new Object[] { url });
            Object vm = RefectUtil.invokeStaticMethod("com.sun.tools.attach.VirtualMachine","attach", vmId);
            logger.info("vm is {}" ,vm.getClass());
            return  vm ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return null;
    }
    private static String getVMId(String vmId){
        return vmId;
    }

    public static void main(String[] args) {
        getTargetVM("13168");
    }

}
