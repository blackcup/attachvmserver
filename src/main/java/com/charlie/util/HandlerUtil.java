package com.charlie.util;

import com.charlie.server.message.AgentParameter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.charlie.common.CommonConf.*;

/**
 * Created by chenc49 on 2018/2/9.
 * Function:
 */

public class HandlerUtil {
    static {
        File file = new File(AGENTPARAMFILE);
        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static byte[] getBytesFromInputStream(InputStream inputStream){
        try {
            byte[] bytes = new byte[MAXBUFFERSIZE];
            int length = inputStream.read(bytes);
            byte[] buffer = new byte[length];
            System.arraycopy(bytes,0,buffer,0,length);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }


    public static AgentParameter matchParameterWithClass(HashMap<String, String> parameterMap,
                                                         HashMap<String, byte[]> classFiles) {
        String vmId = parameterMap.get(VMID_PARAM).trim();
        String classList = parameterMap.get(CLASSNAMES_PARAM);
        String[] split = classList.split(SPLIT_PREFIX);
        if(split == null || split.length == 0){
            return null ;
        }
        AgentParameter parameter = new AgentParameter(vmId);
        Set<String> keys = classFiles.keySet();
        for (int i = 0; i < split.length; i++) {
            String classShortName = getClassShortName(split[i]);
            for (String key : keys) {
                if (key.contains(classShortName)){
                    byte[] classBytes = classFiles.get(key);
                    parameter.getTargetClasses().put(split[i].trim(),classBytes);
                }
            }
        }
        return parameter;
    }

    public static String getClassShortName(String classFullName){
        return classFullName.substring(classFullName.lastIndexOf(".") + 1);
    }

    public static void writeParametrToDisk(AgentParameter parameter) {
        String vmId = parameter.getVmId();

        Map<String, byte[]> targetClasses = parameter.getTargetClasses();
        Set<String> classNames = targetClasses.keySet();
        String agentParam = "";
        for (String className : classNames) {
            byte[] bytes = targetClasses.get(className);
            String filePath  = CLASSFILEPATH + "/" + className;
            boolean b = writeClass(bytes, filePath);
            if (b) {
                agentParam = agentParam + className + SPLIT_PREFIX + filePath + SPLIT_PREFIX + vmId + "\n";
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(AGENTPARAMFILE);
            fileOutputStream.write(agentParam.getBytes(ENCODING));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static boolean writeClass(byte[] bytes, String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return  false ;
            }
        }
        FileOutputStream fileOutputStream = null ;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            return true ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}