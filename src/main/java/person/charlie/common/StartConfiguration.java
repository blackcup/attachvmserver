package person.charlie.common;

/**
 * Created by chenc49 on 2018/2/28.
 * Function:
 */
public class StartConfiguration {
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "StartConfiguration{" +
                "port=" + port +
                '}';
    }
}
