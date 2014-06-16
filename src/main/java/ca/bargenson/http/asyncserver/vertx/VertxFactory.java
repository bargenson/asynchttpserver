package ca.bargenson.http.asyncserver.vertx;

import org.vertx.java.core.Vertx;
import org.vertx.java.platform.PlatformLocator;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 8:56 PM
 */
public abstract class VertxFactory {

    private static final Vertx VERTX = PlatformLocator.factory.createPlatformManager().vertx();

    public static Vertx getInstance() {
        return VERTX;
    }

}
