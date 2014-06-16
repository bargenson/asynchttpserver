package ca.bargenson.http.asyncserver;

import ca.bargenson.http.asyncserver.rest.RestEndpoint;
import ca.bargenson.http.asyncserver.rest.RestResource;
import ca.bargenson.http.asyncserver.vertx.VertxFactory;
import ca.bargenson.http.asyncserver.websocket.DataHandler;
import ca.bargenson.http.asyncserver.websocket.ServerWebSocket;
import ca.bargenson.http.asyncserver.websocket.WebSocketEndpoint;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.WebSocket;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-14
 * Time: 3:50 PM
 */
public class AsyncHttpServerTest {

    private final static String HOST = "localhost";
    private final static int PORT = 8080;
    private final static String APPLICATION_JSON = "application/json";
    private final static String APPLICATION_XML = "application/xml";
    private final static String TEXT_PLAIN = "text/plain";

    private HttpClient httpClient;
    private AsyncHttpServer httpServer;

    @Before
    public void setUp() {
        httpClient = HttpClients.createDefault();
        httpServer = AsyncHttpServer.createServerInstance();
    }

    @After
    public void tearDown() {
        httpServer.close();
    }

    @Test
    public void httpServerEndPoint_should_renderTheResourceAsText() throws IOException {
        // Given
        final String endpointPattern = "/";
        final long time = new Date(123456789).getTime();


        // When
        httpServer.withEndPoint(
                new RestEndpoint(endpointPattern, new RestResource<Long>() {

                    @Override
                    public Long renderResource(HttpRequest request) {
                        return time;
                    }

                })
        ).start(PORT);
        org.apache.http.HttpResponse response = sendHttpRequest("http://" + HOST + ":" + PORT + endpointPattern, TEXT_PLAIN);


        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(EntityUtils.toString(response.getEntity())).isEqualTo(String.valueOf(time));
    }

    @Test
    public void httpServerEndPoint_should_renderSimpleUserAsJson() throws IOException {
        // Given
        final String endpointPattern = "/user";
        final SimpleUser user = new SimpleUser("root", new Date(123456789));


        // When
        httpServer.withEndPoint(
                new RestEndpoint(endpointPattern, new RestResource<SimpleUser>() {

                    @Override
                    public SimpleUser renderResource(HttpRequest request) {
                        return user;
                    }

                })
        ).start(PORT);
        org.apache.http.HttpResponse response = sendHttpRequest("http://" + HOST + ":" + PORT + endpointPattern, APPLICATION_JSON);


        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(EntityUtils.toString(response.getEntity())).isEqualTo("" +
                "{\n" +
                "  \"username\" : \"root\",\n" +
                "  \"dateOfBirth\" : 123456789\n" +
                "}"
        );
    }

    @Test
    public void httpServerEndPoint_should_renderSimpleUserAsXml() throws IOException {
        // Given
        final String endpointPattern = "/user";
        final SimpleUser user = new SimpleUser("root", new Date(123456789));


        // When
        httpServer.withEndPoint(
                new RestEndpoint(endpointPattern, new RestResource<SimpleUser>() {

                    @Override
                    public SimpleUser renderResource(HttpRequest request) {
                        return user;
                    }

                })
        ).start(PORT);
        org.apache.http.HttpResponse response = sendHttpRequest("http://" + HOST + ":" + PORT + endpointPattern, APPLICATION_XML);


        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(EntityUtils.toString(response.getEntity())).isEqualTo("" +
                "<SimpleUser>\n" +
                "  <username>root</username>\n" +
                "  <dateOfBirth>123456789</dateOfBirth>\n" +
                "</SimpleUser>"
        );
    }

    @Test
    public void httpServerEndPoint_should_returnUsernameParam() throws IOException {
        // Given
        final String paramName = "username";
        final String paramValue = "jdoe";
        final String endpointPattern = "/user/:" + paramName;
        final String requestedPath = "/user/" + paramValue;

        // When
        httpServer.withEndPoint(
                new RestEndpoint(endpointPattern, new RestResource<String>() {

                    @Override
                    public String renderResource(HttpRequest request) {
                        return request.paramValue(paramName);
                    }

                })
        ).start(PORT);
        org.apache.http.HttpResponse response = sendHttpRequest("http://" + HOST + ":" + PORT + requestedPath, TEXT_PLAIN);


        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(EntityUtils.toString(response.getEntity())).isEqualTo(paramValue);
    }

    @Test
    public void httpServer_should_returnError404OnUnknownEndpoint() throws IOException {
        // Given
        final String endpointPattern = "/user";
        final String requestedPath = "/badPath";

        // When
        httpServer.withEndPoint(
                new RestEndpoint(endpointPattern, new RestResource<String>() {

                    @Override
                    public String renderResource(HttpRequest request) {
                        return "";
                    }

                })
        ).start(PORT);
        org.apache.http.HttpResponse response = sendHttpRequest("http://" + HOST + ":" + PORT + requestedPath, APPLICATION_XML);


        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(404);
    }

    @Test
    public void httpServerWebSocket_should_multiplyWhatTheClientSendByTwo() throws IOException, InterruptedException, TimeoutException {
        // Given
        final String serverPath = "/multiply";
        final int clientData1 = 2;
        final int clientData2 = 4;


        // When
        httpServer.withEndPoint(
                new WebSocketEndpoint(serverPath).dataHandler(
                        new DataHandler() {

                            @Override
                            public void handle(byte[] data, ServerWebSocket webSocket) {
                                int valueToMultiply = Integer.parseInt(new String(data));
                                webSocket.write(String.valueOf(valueToMultiply * 2).getBytes());
                            }

                        }
                )
        ).start(PORT);

        final WebSocket webSocket = connectToWebSocket(HOST, PORT, serverPath);
        byte[] result = sendDataAndWaitResultOnWebSocket(String.valueOf(clientData1).getBytes(), webSocket);
        int serverData1 = Integer.parseInt(new String(result));
        result = sendDataAndWaitResultOnWebSocket(String.valueOf(clientData2).getBytes(), webSocket);
        int serverData2 = Integer.parseInt(new String(result));


        // Then
        assertThat(serverData1).isEqualTo(clientData1 * 2);
        assertThat(serverData2).isEqualTo(clientData2 * 2);
    }

    @Test(expected = TimeoutException.class)
    public void httpServerWebSocket_should_rejectConnectionOnUnknownEndpoint() throws IOException, InterruptedException, TimeoutException {
        // Given
        final String serverPath = "/multiply";
        final String requestedPath = "/badPath";


        // When
        httpServer.withEndPoint(
                new WebSocketEndpoint(serverPath).dataHandler(
                        new DataHandler() {

                            @Override
                            public void handle(byte[] data, ServerWebSocket webSocket) {
                                webSocket.write("Coucou".getBytes());
                            }

                        }
                )
        ).start(PORT);

        connectToWebSocket(HOST, PORT, requestedPath);
    }


    private org.apache.http.HttpResponse sendHttpRequest(String url, String acceptHeader) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", acceptHeader);
        return httpClient.execute(httpGet);
    }

    private WebSocket connectToWebSocket(String host, int port, String path) throws InterruptedException, TimeoutException {
        final CountDownLatch lock = new CountDownLatch(1);
        Vertx vertx = VertxFactory.getInstance();
        org.vertx.java.core.http.HttpClient client = vertx.createHttpClient().setHost(host).setPort(port);
        final WebSocket[] webSocket = new WebSocket[1];
        client.connectWebsocket(path, new Handler<WebSocket>() {

            @Override
            public void handle(WebSocket ws) {
                webSocket[0] = ws;
                lock.countDown();
            }

        });
        lock.await(2000, TimeUnit.MILLISECONDS);
        if (webSocket[0] == null) {
            throw new TimeoutException("WebSocket connection failed");
        }
        return webSocket[0];
    }

    private byte[] sendDataAndWaitResultOnWebSocket(byte[] data, WebSocket webSocket) throws InterruptedException {
        final CountDownLatch lock = new CountDownLatch(1);
        final byte[][] result = new byte[1][];
        webSocket.dataHandler(new Handler<Buffer>() {

            @Override
            public void handle(Buffer buffer) {
                result[0] = buffer.getBytes();
                lock.countDown();
            }

        });
        webSocket.write(new Buffer(data));
        lock.await(2000, TimeUnit.MILLISECONDS);
        return result[0];
    }

}
