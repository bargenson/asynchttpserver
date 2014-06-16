package ca.bargenson.http.asyncserver.websocket;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 6:54 PM
 */
public class WebSocketEndpoint {

    private final String path;
    private DataHandler dataHandler;

    public WebSocketEndpoint(String path) {
        this.path = path;
    }

    public WebSocketEndpoint dataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
        return this;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public String getPath() {
        return path;
    }
}
