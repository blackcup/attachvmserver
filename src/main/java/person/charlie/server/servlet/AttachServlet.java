package person.charlie.server.servlet;

import person.charlie.server.message.AgentParameter;
import person.charlie.util.AgentUtil;
import person.charlie.util.CommonUtil;
import person.charlie.vm.VMTool;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import static person.charlie.common.CommonConf.*;

/**
 * Created by charlie on 2018/2/8.
 * Function:
 */
public class AttachServlet extends HttpServlet {
    private static boolean initFailed = false;
    private static Throwable throwable = null;


    @Override
    public void init() throws ServletException {
        super.init();
        try {
            createFile();
        } catch (Exception e) {
            throwable = e;
            initFailed = true;
        }
    }

    public static void createFile() throws Exception{
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
            agentParamFile.createNewFile();
        }
        File errorFile = new File(AGENTLOGPATH);
        if (!errorFile.exists()) {
            errorFile.createNewFile();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (initFailed) {
            resp.getWriter().write(CommonUtil.getErroResponse(throwable));
        }
        RequestContext requestContext = new ServletRequestContext(req);
        if (FileUpload.isMultipartContent(requestContext )) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File(REPOSITORY));
            ServletFileUpload fileUpload = new ServletFileUpload();
            fileUpload.setFileItemFactory(factory);
            try {
                List<FileItem> fileItems = fileUpload.parseRequest(req);
                if(fileItems == null || fileItems.size()==0){
                    resp.getWriter().print("No parameter");
                    return ;
                }
                HashMap<String, String> parameterMap = new HashMap<String, String>(16);
                HashMap<String, byte[]> classFiles = new HashMap<String, byte[]>(16);
                for (FileItem item : fileItems) {
                    if (item.isFormField()) {
                        String fieldName = item.getFieldName();
                        String value = item.getString(ENCODING);
                        parameterMap.put(fieldName,value);
                    }else{
                        String fileName = item.getName();
                        InputStream inputStream = item.getInputStream();
                        byte[] bytes = AgentUtil.getBytesFromInputStream(inputStream);
                        classFiles.put(fileName,bytes);
                    }
                }
                AgentParameter parameter = AgentUtil.matchParameterWithClass(parameterMap,classFiles);
                AgentUtil.writeParametrToDisk(parameter);
                VMTool.applyChange(parameter.getVmId());
                String res = "attach successfully" + "\n" + "attach log is :\n" + CommonUtil.getAttachErrorMessage();
                resp.getWriter().print(res);
            } catch (Exception e) {
                String res = "attach failed" + "\n" + "attach log is \n=========================\n" + CommonUtil.getAttachErrorMessage() + "\n";
                res = res + "===============================\n" ;
                res= res + "program log is :\n==========================\n" + CommonUtil.getErroResponse(e);
                resp.getWriter().print(res);
            }
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Please use post way!");
    }


}
