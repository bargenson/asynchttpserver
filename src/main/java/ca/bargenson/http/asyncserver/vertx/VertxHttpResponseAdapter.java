package ca.bargenson.http.asyncserver.vertx;

import ca.bargenson.http.asyncserver.HttpResponse;
import org.vertx.java.core.http.HttpServerResponse;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 7:07 PM
 */
public class VertxHttpResponseAdapter implements HttpResponse {

    private HttpServerResponse serverResponse;

    public VertxHttpResponseAdapter(HttpServerResponse response) {
        this.serverResponse = response;
    }

    @Override
    public void end(String output) {
        serverResponse.end(output);
    }

}
