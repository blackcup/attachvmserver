package person.charlie.vm;

import person.charlie.common.CommonConf;
import person.charlie.common.SystemProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by charlie on 2018/2/8.
 * Function:
 */
public class VMTool {

    private static final Logger logger = LoggerFactory.getLogger(CommonConf.class);
    public static void applyChange(String vmId) throws Exception {
        VirtualMachine vm_instance = VirtualMachine.attach(vmId);
        logger.info("has get attach on {},VM instance is {}",vmId,vm_instance);
        String jar = locateAgentJar();
        vm_instance.loadAgent(jar);
        logger.info("has loaded the agent jar");
        Thread.sleep(1000);
        vm_instance.detach();
        logger.info("has detach the vm:{}",vm_instance);
    }
    private static String locateAgentJar(){
        //current jar is agent jar
        File file = new File("");
        String absolutePath = file.getAbsolutePath();
        logger.info("getAbsolutePath:{}",absolutePath);
        String property = System.getProperty("java.class.path");
        return absolutePath + SystemProperty.FILE_SEPARATOR + property;
    }
}
