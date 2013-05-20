package meez.rxvertx.java.http;

import meez.rxvertx.java.impl.ResultMemoizeHandler;
import meez.rxvertx.java.impl.SingleObserverHandler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import rx.Observable;

/** RxHttpServer */
public class RxHttpServer  {
  
  /** Nested */
  private final HttpServer core;
  
  /** Create new RxHttpServer */
  public RxHttpServer(HttpServer server) {
    this.core=server;
  }
  
  /** Return code */
  public HttpServer coreHttpServer() {
    return this.core;
  }
  
  // RxJava Extensions
  
  public Observable<RxHttpServerRequest> requestHandler() {
    return Observable.create(
      new SingleObserverHandler<RxHttpServerRequest, HttpServerRequest>() {
          public void register() {
            core.requestHandler(this);
          }
          public void clear() {
            core.requestHandler(null);
          }
          public RxHttpServerRequest wrap(HttpServerRequest r) {
            return new RxHttpServerRequest(r);
          }
        }.subscribe      
    );
  }

  public Observable<ServerWebSocket> websocketHandler() {
    return Observable.create(
      new SingleObserverHandler<ServerWebSocket, ServerWebSocket>() {
          public void register() {
            core.websocketHandler(this);
          }
          public void clear() {
            core.websocketHandler(null);
          }
          public ServerWebSocket wrap(ServerWebSocket s) {
            return s;
          }
        }.subscribe      
    );
  }

  public Observable<Void> close() {
    ResultMemoizeHandler<Void> rh=new ResultMemoizeHandler<Void>();
    core.close(rh);
    return Observable.create(rh.subscribe);
  }
}
