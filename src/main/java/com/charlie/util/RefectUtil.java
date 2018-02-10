package com.charlie.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chenc49 on 2018/2/10.
 * Function:
 */
public class RefectUtil {
    public static Object invokeVirtualMethod(Object instance,String methodName,Object ... param){
        Method method = null;
        try {
            Class[] classByInstance = getClassByInstance(param);
            method = getExeableMethod(instance, methodName, classByInstance);
            Object obj = method.invoke(instance, param);
            return obj ;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null ;
    }
    public static Object invokeStaticMethod(String className,String methodName,Object ... param){
        try {
            Class<?> clazz = Class.forName(className);
            return invokeStaticMethod(clazz,methodName,param);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }
    public static Object invokeStaticMethod(Class<?> clazz,String methodName,Object ... param){
        Class[] classByInstance = getClassByInstance(param);
        Method exeableMethod = getExeableMethod(clazz, methodName, classByInstance);
        try {
            Object invoke = exeableMethod.invoke(null, methodName, param);
            return  invoke ;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Class[] getClassByInstance(Object ...param){
        Class<?> [] classes = null;
        if (param == null || param.length == 0) {
            return null;
        }else{
            classes = new Class[param.length];
            for (int i = 0; i < classes.length; i++) {
                classes[i] = param[i].getClass();
            }
        }
        return classes ;
    }
    public static Method getExeableMethod(Object instance,String methodName,Class[] paramTypes){
        return getExeableMethod(instance.getClass(),methodName,paramTypes);
    }
    public static Method getExeableMethod(Class<?> clazz,String methodName,Class[] paramTypes){
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
