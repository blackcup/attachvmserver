package person.charlie.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by charlie on 2018/2/9.
 * Function:
 */
public  final  class CommonConf {
    public static final Logger logger = LoggerFactory.getLogger(CommonConf.class);
    public static final int MAXBUFFERSIZE = 1024*1024*20;
    public static final String VMID_PARAM = "vmId";
    public static final String CLASSNAMES_PARAM = "classNames";
    public static final String SPLIT_PREFIX = ";";
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
        createFile();
        logger.info("CLASSFILEPATH = {},AGENTPARAMFILE = {}",CLASSFILEPATH,AGENTPARAMFILE);
    }
    public static void createFile(){
        File repos = new File(REPOSITORY);
        if(!repos.exists()){
            repos.mkdir();
        }
        File classFilePath = new File(CLASSFILEPATH);
        if(!classFilePath.exists()){
            classFilePath.mkdir();
            logger.info("{} is created",CLASSFILEPATH);
        }
        File agentParamFile = new File(AGENTPARAMFILE);
        if(!agentParamFile.exists()){
            try {
                agentParamFile.createNewFile();
            } catch (IOException e) {
                logger.info("{} failed to create",AGENTPARAMFILE);
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
