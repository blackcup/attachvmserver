package person.charlie.server.servlet;

import person.charlie.server.message.AgentParameter;
import person.charlie.util.AgentUtil;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                resp.getWriter().print("attach successfully");
            } catch (Exception e) {
                resp.getWriter().print(getErroResponse(e));
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Please use post way!");
    }

    String getErroResponse(Exception e){
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder("attach failed because of :");
        stringBuilder.append(e.toString());
        stringBuilder.append("stackTrace:\n");
        String stackString = getStackString(stackTrace);
        stringBuilder.append(stackString);
        return stringBuilder.toString();
    }
    String getStackString(StackTraceElement[] stackTrace){
        StringBuilder stringBuilder = new StringBuilder("");
        for (StackTraceElement stackTraceElement : stackTrace) {
            stringBuilder.append(stackTraceElement.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
