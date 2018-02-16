package com.charlie.common;

import java.io.File;

/**
 * Created by charlie on 2018/2/9.
 * Function:
 */
public class SystemProperty{
    public static final String JAVA_HOME;
    public static final String TOOLS_PATH;
    public static final OSType OS_TYPE;
    public static final String FILE_SEPARATOR;
    static {
        JAVA_HOME = System.getProperty("java.home");
        String property = System.getProperty("os.name");
        String os = System.getProperty("os.name").toUpperCase();
        if (os.contains("WINDOWS")){
            OS_TYPE = OSType.WINDOWS;
            FILE_SEPARATOR = "\\";
        }else if (os.contains("LINUX")){
            OS_TYPE = OSType.LINUX;
            FILE_SEPARATOR = "/";
        }else {
            OS_TYPE = OSType.OTHER;
            FILE_SEPARATOR = "/";
        }
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

    public static void main(String[] args) {

    }
}
