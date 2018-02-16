package com.charlie.server;

import com.charlie.server.servlet.AttachServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by charlie on 2018/2/8.
 * Function:
 */
public class AttachServer {
    public static Logger logger = LoggerFactory.getLogger(AttachServer.class);
    private static  final int PORT = 8080;
    public static void main(String[] args) {
        Server server = new Server(8080);
        ServletContextHandler contextHandler = new ServletContextHandler();
        server.setHandler(contextHandler);
        contextHandler.addServlet(AttachServlet.class,"/uploadFile");
        try {
            server.start();
            logger.info("server has started on {},serverId:{}",PORT,server.toString());
            server.join();
        } catch (Exception e) {
            logger.info(e.getMessage());
            System.exit(1);
        }

    }
}
