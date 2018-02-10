package com.charlie.common;

import java.io.File;
import java.io.IOException;

/**
 * Created by chenc49 on 2018/2/9.
 * Function:
 */
public  final  class CommonConf {
    public static final int MAXBUFFERSIZE = 1024*1024*20;
    public static final String VMID_PARAM = "vmId";
    public static final String CLASSNAMES_PARAM = "classNames";
    public static final String SPLIT_PREFIX = ":";
    public static final String REPOSITORY;
    public static final String ENCODING = "UTF-8" ;
    public static final String CLASSFILEPATH;
    public static final String AGENTPARAMFILE ;
    static {
        switch (SystemProperty.OS_TYPE){
            case WINDOWS:{REPOSITORY = "C:\\tmp";CLASSFILEPATH = "C:\\tmp";AGENTPARAMFILE = "C:\\tmp\\vm_attach_class_param";break;}
            case LINUX:
            case OTHER:
            default:{REPOSITORY = "/tmp";CLASSFILEPATH = "/tmp";AGENTPARAMFILE = "/tmp/vm_attach_class_param";break;}
        }

    }
    public static void createFile(){
        File repos = new File(REPOSITORY);
        if(!repos.exists()){
            repos.mkdir();
        }
        File classFilePath = new File(CLASSFILEPATH);
        if(!classFilePath.exists()){
            classFilePath.mkdir();
        }
        File agentParamFile = new File(AGENTPARAMFILE);
        if(!agentParamFile.exists()){
            try {
                classFilePath.createNewFile();
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }
}
