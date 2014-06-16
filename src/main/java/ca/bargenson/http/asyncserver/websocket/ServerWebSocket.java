package ca.bargenson.http.asyncserver.websocket;

import ca.bargenson.http.asyncserver.vertx.VertxWebSocketAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 7:41 PM
 */
public interface ServerWebSocket {

    VertxWebSocketAdapter write(byte[] data);
}
