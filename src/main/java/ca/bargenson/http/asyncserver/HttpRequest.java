package ca.bargenson.http.asyncserver;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 3:16 AM
 */
public interface HttpRequest {

    final static String ACCEPT_HEADER = "Accept";
    final static String APPLICATION_JSON = "application/json";
    final static String APPLICATION_XML = "application/xml";

    boolean acceptJson();

    boolean acceptXml();

    String paramValue(String name);

    List<String> paramValues(String name);

}
