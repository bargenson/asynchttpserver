package ca.bargenson.http.asyncserver.rest;

import ca.bargenson.http.asyncserver.HttpRequest;
import ca.bargenson.http.asyncserver.HttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 3:14 AM
 */
public class RestEndpoint {

    private final String pattern;
    private final RestResource<?> resource;

    public RestEndpoint(String pattern, RestResource<?> resource) {
        this.pattern = pattern;
        this.resource = resource;
    }

    public String getPattern() {
        return pattern;
    }

    public RequestHandler getHandler() {
        return new RequestHandler() {

            @Override
            public void handle(HttpRequest request, HttpResponse response) {
                String output;
                final RestResource<?> resource = getResource();
                if (request.acceptJson()) {
                    output = resource.renderResourceAsJson(request);
                } else if (request.acceptXml()) {
                    output = resource.renderResourceAsXml(request);
                } else {
                    output = resource.renderResourceAsTxt(request);
                }
                response.end(output);
            }

        };
    }

    private RestResource<?> getResource() {
        return resource;
    }
}
