package ca.bargenson.http.asyncserver.websocket;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 8:10 PM
 */
public interface DataHandler {

    void handle(byte[] data, ServerWebSocket webSocket);

}
