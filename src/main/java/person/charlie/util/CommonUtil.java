package person.charlie.util;

public class CommonUtil {
    public static String  getErroResponse(Exception e){
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
}
