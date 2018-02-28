package person.charlie.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.charlie.common.CommonConf;

import java.io.*;
import java.lang.instrument.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlie on 2018/2/8.
 * Function:
 */
public class AgentImp{

    /***
     *
     * change class in runtime
     * @param target className
     * @param instrumentation
     */
    public static final Logger logger = LoggerFactory.getLogger(AgentImp.class);
    public static void agentmain(String target,Instrumentation instrumentation){
        //target classes to need to change
        List<TargetClass> targetClasses = getTargetClasses();
        //check vm has already loaded this class
       Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
        checkLoaded(targetClasses);
        //get class bytes from  TargetClass List ,it contains where the new class bytes is,see@bytesPath
       ClassDefinition[] classDefinitions = loadNewBytesForTargetClass(targetClasses,allLoadedClasses);
        try {
            //redefine the target class
            instrumentation.redefineClasses(classDefinitions);
        } catch (Exception e) {
            logger.error("failed to load agent :{}",e.getMessage());
        }
    }


    private static void checkLoaded(List<TargetClass> targetClasses ){

    }

    /***
     * className:bytesPath:vmId
     * @return
     */
    private static List<TargetClass> getTargetClasses(){
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(CommonConf.AGENTPARAMFILE));
            String line;
            ArrayList<TargetClass> targetClassProperties = new ArrayList<TargetClass>(16);
            while ((line = bufferedReader.readLine())!= null){
                if (line.trim().equals("")) {
                    continue;
                }
                String[] split = line.split(CommonConf.SPLIT_PREFIX);
                TargetClass targetClass = new TargetClass(split[0], split[1], split[2]);
                targetClassProperties.add(targetClass);
            }

            return targetClassProperties.size()!=0 ? targetClassProperties : null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    private static ClassDefinition[] loadNewBytesForTargetClass(List<TargetClass> targetClassNames, Class[] allLoadedClasses){
        if(targetClassNames == null || targetClassNames.size() == 0){
            return null ;
        }
        ArrayList<ClassDefinition> list = new ArrayList<ClassDefinition>(16);
        for (Class clazz : allLoadedClasses) {
            String name = clazz.getName();
            for (TargetClass targetClass : targetClassNames) {
                String className = targetClass.getClassName();
                if(name.equals(className)){
                    byte[] bytes = loadNewClassBytes(targetClass.getBytesPath());
                    list.add(new ClassDefinition(clazz,bytes));
                }
            }
        }
        int length = list.size();
        ClassDefinition[] classDefinitions = new ClassDefinition[length];
        for (int i = 0; i < list.size(); i++) {
            classDefinitions[i] = list.get(i);
        }
        return classDefinitions;
    }
    private static Class<?> getClassByName(String className,Class[]allClasses){
        for (Class clazz : allClasses) {
            String clazzName = clazz.getName();
            System.out.println("clazzName = " + clazzName);
            if(clazzName.equals(className)){
                return  clazz ;
            }
        }
        return null;
    }
    private static byte[] loadNewClassBytes(String path){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            byte[] bytes = new byte[CommonConf.MAXBUFFERSIZE];
            int length = fileInputStream.read(bytes);
            byte[] buffer = new byte[length];
            System.arraycopy(bytes,0,buffer,0,length);

            return buffer;
        } catch (FileNotFoundException e) {
            logger.info("path={}",path,e);
        } catch (IOException e) {
            logger.info("path={}",path,e);
        }finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error("can not locate the file:{}",path,e);
                }
            }
        }
        return null;
    }

}
