package com.charlie.vm;

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
        releaseAgent(vm);
    }

    private static void releaseAgent(Object vm) {
    }

    private static void attachAgent(Object vm) {
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
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            add.setAccessible(true);
            URLClassLoader classloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            URL url = new File("C:\\Program Files\\Java\\jdk1.8.0_121\\lib\\tools.jar").toURI().toURL();
            add.invoke(classloader, new Object[] { url });
            Class<?> clazz = Class.forName("com.sun.tools.attach.VirtualMachine");
            Method method = clazz.getDeclaredMethod("attach",String.class);
            Object invoke = method.invoke(null, "8732");
            System.out.println(invoke);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.home"));
        System.out.println(System.getProperty("os.name"));
        getTargetVM("");
    }
}
