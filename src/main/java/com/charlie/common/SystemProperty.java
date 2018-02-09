package com.charlie.common;

import java.io.File;

/**
 * Created by chenc49 on 2018/2/9.
 * Function:
 */
public class SystemProperty{
    public static final String JAVA_HOME;
    public static final String TOOLS_PATH;
    public static final String OS_TYPE;
    static {
        JAVA_HOME = System.getProperty("java.home");
        OS_TYPE = System.getProperty("os.name");
        TOOLS_PATH = getToolsPath();
    }
    public static String getToolsPath(){
        File java_home = new File(JAVA_HOME);
        File parentFile = java_home.getParentFile();
        File[] files = parentFile.listFiles();
        for (File file : files) {
            if (file.getName().equals("lib")) {
                File[] libs = file.listFiles();
                for (File lib : libs) {
                    if(lib.getName().equals("tools.jar")){
                        return lib.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }
}
