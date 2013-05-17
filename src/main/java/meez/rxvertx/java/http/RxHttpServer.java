package meez.rxvertx.java.http;

import meez.rxvertx.java.subject.ReplySubject;
import meez.rxvertx.java.subject.StreamSubject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.*;
import rx.Observable;

/** RxHttpServer */
public class RxHttpServer  {
  
  /** Nested */
  private final HttpServer nested;
  
  /** Create new RxHttpServer */
  public RxHttpServer(HttpServer server) {
    this.nested=server;
  }
  
  /** Return code */
  public HttpServer coreHttpServer() {
    return this.nested;
  }
  
  // RxJava Extensions
  
  public Observable<RxHttpServerRequest> requestHandler() {
    final StreamSubject<RxHttpServerRequest> rx=StreamSubject.create();
    nested.requestHandler(new Handler<HttpServerRequest>() {
      public void handle(HttpServerRequest httpReq) {
        rx.handle(new RxHttpServerRequest(httpReq));
      }
    });
    return rx;
  }

  public Observable<ServerWebSocket> websocketHandler() {
    StreamSubject<ServerWebSocket> rx=StreamSubject.create();
    nested.websocketHandler(rx);
    return rx;
  }

  public Observable<Void> close() {
    ReplySubject<Void> rx=ReplySubject.create();
    nested.close(rx);
    return rx;
  }
}
