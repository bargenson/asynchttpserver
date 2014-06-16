import ca.bargenson.http.asyncserver.AsyncHttpServer;
import ca.bargenson.http.asyncserver.HttpRequest;
import ca.bargenson.http.asyncserver.SimpleUser;
import ca.bargenson.http.asyncserver.rest.RestEndpoint;
import ca.bargenson.http.asyncserver.rest.RestResource;
import ca.bargenson.http.asyncserver.websocket.DataHandler;
import ca.bargenson.http.asyncserver.websocket.ServerWebSocket;
import ca.bargenson.http.asyncserver.websocket.WebSocketEndpoint;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 10:30 PM
 */
public class StartServerAndWait {

    public static void main(String... args) throws InterruptedException {
        AsyncHttpServer httpServer = AsyncHttpServer.createServerInstance().withEndPoint(
                new RestEndpoint("/helloWorld", new RestResource<String>() {

                    @Override
                    protected String renderResource(HttpRequest request) {
                        return "Hello World";
                    }

                })
        ).withEndPoint(
                new RestEndpoint("/users", new RestResource<List<SimpleUser>>() {

                    @Override
                    protected List<SimpleUser> renderResource(HttpRequest request) {
                        return Arrays.asList(
                                new SimpleUser("jdoe", new Date(987654321123456L)),
                                new SimpleUser("edupond", new Date(987634321123446L)),
                                new SimpleUser("imorin", new Date(98745321123446L))
                        );
                    }

                })
        ).withEndPoint(
                new RestEndpoint("/users/:username", new RestResource<SimpleUser>() {

                    @Override
                    protected SimpleUser renderResource(HttpRequest request) {
                        final String username = request.paramValue("username");
                        return new SimpleUser(username, new Date(98745321123446L));
                    }

                })
        ).withEndPoint(
                new WebSocketEndpoint("/websocket").dataHandler(new DataHandler() {

                    @Override
                    public void handle(byte[] data, ServerWebSocket webSocket) {
                        webSocket.write(data);
                    }

                })
        ).start(8080);

        synchronized (httpServer) {
            httpServer.wait();
        }
    }

}
