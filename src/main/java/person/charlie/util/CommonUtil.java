package person.charlie.util;

import person.charlie.common.CommonConf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CommonUtil {
    public static String  getErroResponse(Throwable e){
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder("attach failed because of :");
        stringBuilder.append(e.toString());
        stringBuilder.append("stackTrace:\n");
        String stackString = getStackString(stackTrace);
        stringBuilder.append(stackString);
        return stringBuilder.toString();
    }
    public static String getStackString(StackTraceElement[] stackTrace){
        StringBuilder stringBuilder = new StringBuilder("");
        for (StackTraceElement stackTraceElement : stackTrace) {
            stringBuilder.append(stackTraceElement.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    public static String getAttachErrorMessage(){
        FileReader fileReader = null ;
        try {
            fileReader = new FileReader(CommonConf.AGENTLOGPATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            String message = "";
            while ((line = bufferedReader.readLine())!=null){
                message = message + line + "\n";
            }
            return message;
        } catch (Exception e) {
            return "failed to get attach log: " + e.toString();
        }finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
