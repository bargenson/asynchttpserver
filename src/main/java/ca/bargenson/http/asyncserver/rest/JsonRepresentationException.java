package ca.bargenson.http.asyncserver.rest;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 6:01 PM
 */
public class JsonRepresentationException extends RuntimeException {

    public JsonRepresentationException(String message, Exception cause) {
        super(message, cause);
    }

}
