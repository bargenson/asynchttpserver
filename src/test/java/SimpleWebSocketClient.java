import ca.bargenson.http.asyncserver.vertx.VertxFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.WebSocket;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 10:58 PM
 */
public class SimpleWebSocketClient {

    public static void main(String... args) throws TimeoutException, InterruptedException {
        final WebSocket webSocket = connectToWebSocket("localhost", 8080, "/websocket");
        while (true) {
            String input = new Scanner(System.in).nextLine();
            final byte[] bytes = sendDataAndWaitResultOnWebSocket(input.getBytes(), webSocket);
            System.out.println(new String(bytes));
        }

    }

    private static WebSocket connectToWebSocket(String host, int port, String path) throws InterruptedException, TimeoutException {
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

    private static byte[] sendDataAndWaitResultOnWebSocket(byte[] data, WebSocket webSocket) throws InterruptedException {
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
