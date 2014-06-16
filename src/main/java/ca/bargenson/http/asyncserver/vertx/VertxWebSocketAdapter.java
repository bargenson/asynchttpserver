package ca.bargenson.http.asyncserver.vertx;

import ca.bargenson.http.asyncserver.websocket.ServerWebSocket;
import org.vertx.java.core.buffer.Buffer;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 7:26 PM
 */
public class VertxWebSocketAdapter implements ServerWebSocket {

    private org.vertx.java.core.http.ServerWebSocket serverWebSocket;

    public VertxWebSocketAdapter(org.vertx.java.core.http.ServerWebSocket serverWebSocket) {
        this.serverWebSocket = serverWebSocket;
    }

    @Override
    public VertxWebSocketAdapter write(final byte[] data) {
        serverWebSocket.write(new Buffer(data));
        return this;
    }

}
