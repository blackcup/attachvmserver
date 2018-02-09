package com.charlie.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenc49 on 2018/2/9.
 * Function:
 */
public class IOUtil {
    private static final int MAXBUFFERSIZE = 1024*1024*20;
    public static byte[] getBytesFromInputStream(InputStream inputStream){
        try {
            byte[] bytes = new byte[MAXBUFFERSIZE];
            int length = inputStream.read(bytes);
            byte[] buffer = new byte[length];
            System.arraycopy(bytes,0,buffer,0,length);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
