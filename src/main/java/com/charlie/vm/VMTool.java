package com.charlie.vm;

import com.charlie.common.SystemProperty;
import com.charlie.util.RefectUtil;

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

    public static void applyChange(String vmId){
        Object vm = getTargetVM(vmId);
        attachAgent(vm);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        detachAgent(vm);
    }

    private static void detachAgent(Object vm) {
        RefectUtil.invokeVirtualMethod(vm,"detach",null);
    }

    private static void attachAgent(Object vm) {
        String agentPath = locateAget();
        RefectUtil.invokeVirtualMethod(vm,"loadAgent",agentPath);
    }

    private static String locateAget(){
        //current jar is agent jar
        String property = System.getProperty("java.class.path");
        return property;
    }

    static class MyClassLoader extends URLClassLoader {

        public MyClassLoader(URL[] urls) {
            super(urls);
        }

        public MyClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        public void addJar(URL url) {
            this.addURL(url);
        }

    }
    private static Object getTargetVM(String vmId) {
        try {
            String toolsPath = SystemProperty.TOOLS_PATH;
            URL url = new File(toolsPath).toURI().toURL();
            //RefectUtil.invokeVirtualMethod(ClassLoader.getSystemClassLoader(),"addURL",url);
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            add.setAccessible(true);
            URLClassLoader classloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            add.invoke(classloader, new Object[] { url });
            Object vm = RefectUtil.invokeStaticMethod("com.sun.tools.attach.VirtualMachine","attach", vmId);
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
