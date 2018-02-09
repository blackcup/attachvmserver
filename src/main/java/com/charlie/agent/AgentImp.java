package com.charlie.agent;

import java.io.*;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.List;

import static com.charlie.common.CommonConf.*;

/**
 * Created by chenc49 on 2018/2/8.
 * Function:
 */
public class AgentImp{

    public static void premain(String target){

    }
    public static void premain(String target, Instrumentation instrumentation){

    }

    public static void agentmain(String target){

    }

    /***
     *
     * change class in runtime
     * @param target className
     * @param instrumentation
     */
    public static void agentmain(String target,Instrumentation instrumentation){

        //target classes to need to change
        List<TargetClassProperty> targetClasses = getTargetClasses();
        //check vm has already loaded this class
        checkLoaded(targetClasses);
        Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
        //get class bytes from  TargetClassProperty List ,it contains where the new class bytes is,see@bytesPath
        ClassDefinition[] classDefinitions = loadNewBytesForTargetClass(targetClasses,allLoadedClasses);
        try {
            //redefine the target class
            instrumentation.redefineClasses(classDefinitions);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        }
    }



    private static void checkLoaded(List<TargetClassProperty> targetClasses ){}

    /***
     * className:bytesPath:vmId
     * @return
     */
    private static List<TargetClassProperty> getTargetClasses(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(AGENTPARAMFILE));
            String line = null ;
            ArrayList<TargetClassProperty> targetClassProperties = new ArrayList<TargetClassProperty>(16);
            while ((line = bufferedReader.readLine())!= null){
                if (line.trim().equals("")) {
                    continue;
                }
                String[] split = line.split(SPLIT_PREFIX);
                TargetClassProperty targetClassProperty = new TargetClassProperty(split[0], split[1], split[2]);
                targetClassProperties.add(targetClassProperty);
            }
            return targetClassProperties.size()!=0 ? targetClassProperties : null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static ClassDefinition[] loadNewBytesForTargetClass(List<TargetClassProperty> targetClassNames,Class[] allLoadedClasses){
        if(targetClassNames == null || targetClassNames.size() == 0){
            return null ;
        }
        int length = targetClassNames.size();
        ClassDefinition[] classDefinitions = new ClassDefinition[length];
        for (int i = 0; i < targetClassNames.size(); i++) {
            String bytesPath = targetClassNames.get(i).getBytesPath();
            byte[] bytes = loadNewClassBytes(bytesPath);
            Class<?> classByName = getClassByName(targetClassNames.get(i).getClassName(), allLoadedClasses);
            classDefinitions[i] = new ClassDefinition(classByName,bytes);
        }
        return classDefinitions;
    }
    private static Class<?> getClassByName(String className,Class[]allClasses){
        for (Class clazz : allClasses) {
            String clazzName = clazz.getName();
            if(clazzName.equals(className)){
                return  clazz ;
            }
        }
        return null;
    }
    private static byte[] loadNewClassBytes(String path){
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            byte[] bytes = new byte[MAXBUFFERSIZE];
            int length = fileInputStream.read(bytes);
            byte[] buffer = new byte[length];
            System.arraycopy(bytes,0,buffer,0,length);
            return buffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
