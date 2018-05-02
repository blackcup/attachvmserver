package person.charlie.upload;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AttachServletTest {
    private static final int BYTEBUFFERLENGTH = 1024*1024*20;
    @Test
    public void testForUploadClass() throws Exception{
        String url = "http://localhost:1234/uploadFile";
        String classFilePath = "C:\\Users\\chenc49\\Desktop\\HelloWorld.class";
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        File classFile = new File(classFilePath);
        Part [] parts = new Part[]{new FilePart("classFile",classFile),new StringPart("vmId","HelloWorlds"),new StringPart("classNames",getClassName(classFilePath))};
        postMethod.setRequestEntity(new MultipartRequestEntity(parts,postMethod.getParams()));
        int i = httpClient.executeMethod(postMethod);
        System.out.println(postMethod.getResponseBodyAsString());
    }
    @Test
    public void getClassNameTest() throws ClassNotFoundException {
        System.out.println(getClassName("C:\\Users\\chenc49\\Desktop\\HelloWorld.class"));
    }
    private String getClassName(String path) throws ClassNotFoundException {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(name);
                    byte[] bytes = new byte[BYTEBUFFERLENGTH];
                    int read = fileInputStream.read(bytes);
                    byte[] buffer = new byte[read];
                    System.arraycopy(bytes,0,buffer,0,read);
                    return defineClass(buffer,0,read);
                } catch (Exception e) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                return null;
            }
        };
        Class<?> aClass = classLoader.loadClass(path);
        if (aClass != null) {
            return aClass.getName();
        }
        return null;
    }
}
