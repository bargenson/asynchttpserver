package ca.bargenson.http.asyncserver;

import ca.bargenson.http.asyncserver.rest.RestEndpoint;
import ca.bargenson.http.asyncserver.vertx.VertxHttpServerAdapter;
import ca.bargenson.http.asyncserver.websocket.WebSocketEndpoint;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-14
 * Time: 3:46 PM
 */
public abstract class AsyncHttpServer {

    public static AsyncHttpServer createServerInstance() {
        return new VertxHttpServerAdapter();
    }

    public abstract AsyncHttpServer withEndPoint(final RestEndpoint endPoint);

    public abstract AsyncHttpServer withEndPoint(final WebSocketEndpoint endPoint);

    public abstract AsyncHttpServer start(int port);

    public abstract void close();

}
