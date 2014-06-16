package ca.bargenson.http.asyncserver.vertx;

import ca.bargenson.http.asyncserver.HttpRequest;
import org.vertx.java.core.http.HttpServerRequest;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 4:38 AM
 */
public class VertxHttpRequestAdapter implements HttpRequest {

    private final HttpServerRequest httpServerRequest;

    public VertxHttpRequestAdapter(HttpServerRequest httpServerRequest) {
        this.httpServerRequest = httpServerRequest;
    }

    @Override
    public boolean acceptJson() {
        return httpServerRequest.headers().get(ACCEPT_HEADER).contains(APPLICATION_JSON);
    }

    @Override
    public boolean acceptXml() {
        return httpServerRequest.headers().get(ACCEPT_HEADER).contains(APPLICATION_XML);
    }

    @Override
    public String paramValue(String name) {
        return httpServerRequest.params().get(name);
    }

    @Override
    public List<String> paramValues(String name) {
        return httpServerRequest.params().getAll(name);
    }


}
