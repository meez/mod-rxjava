package meez.rxvertx.groovy;

import org.vertx.groovy.core.Vertx;

/** Rx wrapper Vertx Groovy */
public class RxVertx extends meez.rxvertx.java.RxVertx {
  
  /** Create new RxVertx */
  public RxVertx(Vertx vertx) {
    super(vertx.toJavaVertx());
  }
}
