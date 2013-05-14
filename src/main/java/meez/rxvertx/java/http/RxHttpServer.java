package meez.rxvertx.java.http;

import meez.rxvertx.java.subject.ReplySubject;
import meez.rxvertx.java.subject.StreamSubject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.*;
import rx.Observable;

/** RxHttpServer */
public class RxHttpServer implements HttpServer {
  
  /** Nested */
  private HttpServer nested;
  
  /** Create new RxHttpServer */
  public RxHttpServer(HttpServer server) {
    this.nested=server;
  }
  
  // RxJava Extensions
  
  public Observable<HttpServerRequest> requestHandlerRx() {
    StreamSubject<HttpServerRequest> rx=StreamSubject.create();
    nested.requestHandler(rx);
    return rx;
  }

  public Observable<ServerWebSocket> websocketHandlerRx() {
    StreamSubject<ServerWebSocket> rx=StreamSubject.create();
    nested.websocketHandler(rx);
    return rx;
  }

  public Observable<Void> closeRx() {
    ReplySubject<Void> rx=ReplySubject.create();
    nested.close(rx);
    return rx;
  }

  // HttpTestClient implementation

  public HttpServer requestHandler(Handler<HttpServerRequest> httpServerRequestHandler) {
    return nested.requestHandler(httpServerRequestHandler);
  }

  public Handler<HttpServerRequest> requestHandler() {
    return nested.requestHandler();
  }

  public HttpServer websocketHandler(Handler<ServerWebSocket> serverWebSocketHandler) {
    nested.websocketHandler(serverWebSocketHandler);
    return this;
  }

  public Handler<ServerWebSocket> websocketHandler() {
    return nested.websocketHandler();
  }

  public HttpServer listen(int i) {
    nested.listen(i);
    return this;
  }

  public HttpServer listen(int i, String s) {
    nested.listen(i,s);
    return this;
  }

  public void close() {
    nested.close();
  }

  public void close(Handler<Void> voidHandler) {
    nested.close(voidHandler);
  }

  public HttpServer setSSL(boolean b) {
    nested.setSSL(b);
    return this;
  }

  public HttpServer setKeyStorePath(String s) {
    nested.setKeyStorePath(s);
    return this;
  }

  public HttpServer setKeyStorePassword(String s) {
    nested.setKeyStorePassword(s);
    return this;
  }

  public HttpServer setTrustStorePath(String s) {
    nested.setTrustStorePath(s);
    return this;
  }

  public HttpServer setTrustStorePassword(String s) {
    nested.setTrustStorePassword(s);
    return this;
  }

  public HttpServer setClientAuthRequired(boolean b) {
    nested.setClientAuthRequired(b);
    return this;
  }

  public HttpServer setTCPNoDelay(boolean b) {
    nested.setTCPNoDelay(b);
    return this;
  }

  public HttpServer setSendBufferSize(int i) {
    nested.setSendBufferSize(i);
    return this;
  }

  public HttpServer setReceiveBufferSize(int i) {
    nested.setReceiveBufferSize(i);
    return this;
  }

  public HttpServer setTCPKeepAlive(boolean b) {
    nested.setTCPKeepAlive(b);
    return this;
  }

  public HttpServer setReuseAddress(boolean b) {
    nested.setReuseAddress(b);
    return this;
  }

  public HttpServer setSoLinger(boolean b) {
    nested.setSoLinger(b);
    return this;
  }

  public HttpServer setTrafficClass(int i) {
    nested.setTrafficClass(i);
    return this;
  }

  public HttpServer setAcceptBacklog(int i) {
    nested.setAcceptBacklog(i);
    return this;
  }

  public Boolean isTCPNoDelay() {
    return nested.isTCPNoDelay();
  }

  public Integer getSendBufferSize() {
    return nested.getSendBufferSize();
  }

  public Integer getReceiveBufferSize() {
    return nested.getReceiveBufferSize();
  }

  public Boolean isTCPKeepAlive() {
    return nested.isTCPKeepAlive();
  }

  public Boolean isReuseAddress() {
    return nested.isReuseAddress();
  }

  public Boolean isSoLinger() {
    return nested.isSoLinger();
  }

  public Integer getTrafficClass() {
    return nested.getTrafficClass();
  }

  public Integer getAcceptBacklog() {
    return nested.getAcceptBacklog();
  }

  public boolean isSSL() {
    return nested.isSSL();
  }

  public String getKeyStorePath() {
    return nested.getKeyStorePath();
  }

  public String getKeyStorePassword() {
    return nested.getKeyStorePassword();
  }

  public String getTrustStorePath() {
    return nested.getTrustStorePath();
  }

  public String getTrustStorePassword() {
    return nested.getTrustStorePassword();
  }
}
