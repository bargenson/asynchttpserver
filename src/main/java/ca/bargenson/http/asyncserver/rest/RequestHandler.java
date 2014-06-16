package ca.bargenson.http.asyncserver.rest;

import ca.bargenson.http.asyncserver.HttpRequest;
import ca.bargenson.http.asyncserver.HttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 6:59 PM
 */
public abstract class RequestHandler {

    public abstract void handle(HttpRequest request, HttpResponse response);

}
