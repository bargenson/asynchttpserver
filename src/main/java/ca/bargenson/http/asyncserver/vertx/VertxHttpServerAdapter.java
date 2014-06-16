package ca.bargenson.http.asyncserver.vertx;

import ca.bargenson.http.asyncserver.AsyncHttpServer;
import ca.bargenson.http.asyncserver.rest.RestEndpoint;
import ca.bargenson.http.asyncserver.websocket.WebSocketEndpoint;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.http.ServerWebSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 6:37 PM
 */
public class VertxHttpServerAdapter extends AsyncHttpServer {

    private RouteMatcher routeMatcher;
    private HttpServer httpServer;
    private List<WebSocketEndpoint> webSocketEndpoints;

    public VertxHttpServerAdapter() {
        Vertx vertx = VertxFactory.getInstance();
        routeMatcher = new RouteMatcher();
        httpServer = vertx.createHttpServer();
        webSocketEndpoints = new ArrayList<WebSocketEndpoint>();
    }

    public VertxHttpServerAdapter withEndPoint(final RestEndpoint restEndpoint) {
        routeMatcher.all(restEndpoint.getPattern(), new org.vertx.java.core.Handler<HttpServerRequest>() {

            @Override
            public void handle(HttpServerRequest request) {
                restEndpoint.getHandler().handle(new VertxHttpRequestAdapter(request), new VertxHttpResponseAdapter(request.response()));
            }

        });
        return this;
    }

    public VertxHttpServerAdapter withEndPoint(final WebSocketEndpoint webSocketEndpoint) {
        webSocketEndpoints.add(webSocketEndpoint);
        return this;
    }

    public VertxHttpServerAdapter start(int port) {
        httpServer.requestHandler(routeMatcher)
                .websocketHandler(new org.vertx.java.core.Handler<ServerWebSocket>() {

                    @Override
                    public void handle(final ServerWebSocket webSocket) {
                        boolean existingPath = false;
                        String requestedPath = webSocket.path();
                        for (final WebSocketEndpoint endpoint : webSocketEndpoints) {
                            if (requestedPath.equals(endpoint.getPath())) {
                                webSocket.dataHandler(new Handler<Buffer>() {

                                    @Override
                                    public void handle(Buffer buffer) {
                                        endpoint.getDataHandler().handle(buffer.getBytes(), new VertxWebSocketAdapter(webSocket));
                                    }

                                });
                                existingPath = true;
                                break;
                            }
                        }
                        if (!existingPath) {
                            webSocket.reject();
                        }

                    }

                })
                .listen(port);
        return this;
    }

    public void close() {
        httpServer.close();
    }

}
