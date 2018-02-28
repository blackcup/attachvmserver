package person.charlie.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.charlie.common.StartConfiguration;
import person.charlie.server.servlet.AttachServlet;

/**
 * Created by charlie on 2018/2/8.
 * Function:
 */
public class AttachServer {
    public static Logger logger = LoggerFactory.getLogger(AttachServer.class);
    private static  final int PORT = 2018;
    private static final String SPLIT = "=" ;
    public static void main(String[] args) {
        StartConfiguration startConfiguration = new StartConfiguration();
        if (args == null || args.length == 0) {
            setDefaultConfiguration(startConfiguration);
        }else{
            loadConfiguration(args,startConfiguration);
        }
        Server server = new Server(startConfiguration.getPort());
        ServletContextHandler contextHandler = new ServletContextHandler();
        server.setHandler(contextHandler);
        contextHandler.addServlet(AttachServlet.class,"/uploadFile");
        try {
            server.start();
            logger.info("server has started on {},serverId:{}",startConfiguration.getPort(),server.toString());
            server.join();
        } catch (Exception e) {
            logger.info(e.getMessage());
            System.exit(1);
        }
    }



    private static void setDefaultConfiguration(StartConfiguration startConfiguration){
        startConfiguration.setPort(PORT);
    }
    private static void loadConfiguration(String []args,StartConfiguration startConfiguration){
        for (String arg : args) {
            if (arg.trim().equals("")){
                continue;
            }
            String[] split = arg.split(SPLIT);
            if(split.length<1 ||split.length > 3){
                System.out.println("wrong parameter:" + args);
                System.exit(0);
            }else if(split.length==1){
                if(split[0].equals("-h")){
                    printHelp();
                    System.exit(0);
                }
            } else if (split.length==2){
                String key = split[0];
                String value = split[1];
                if (key.equals("-p")){
                    try {
                        int port = Integer.parseInt(value);
                        startConfiguration.setPort(port);
                    } catch (NumberFormatException e) {
                        System.out.println(value + " is not a number");
                        System.exit(0);
                    }
                }else{
                    System.out.println("not supported parameter : " + key);
                    printHelp();
                    System.exit(0);
                }
            }
        }
    }

    private static void printHelp(){
        System.out.println("attach vm server usage:");
        System.out.println("    -p      the port the server will start on.If not be specialized,will start on 2018" );
        System.out.println("    -h      show this help");
    }

}
