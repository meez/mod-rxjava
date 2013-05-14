package meez.rxvertx.java.http;

import java.util.Map;

import meez.rxvertx.java.subject.ReplySubject;
import meez.rxvertx.java.subject.StreamSubject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.*;
import rx.Observable;
import rx.util.functions.Action1;

/** RxWrapper for HttpClient */
public class RxHttpClient implements HttpClient {
  
  /** Nested */
  private final HttpClient nested;
  
  /** Create new RxHttpClient */
  public RxHttpClient(HttpClient nested) {
    this.nested=nested;
  }

  // Rx extensions
  
  /** General exception handler */
  public Observable<Exception> exceptionHandlerRx() {
    StreamSubject<Exception> rx=StreamSubject.create();
    nested.exceptionHandler(rx);
    return rx;
  }

  /** Connect to WebSocket */
  public Observable<WebSocket> connectWebsocketRx(String s) {
    ReplySubject<WebSocket> rx=ReplySubject.create();
    nested.connectWebsocket(s,rx);
    return rx;
  }

  /** Connect to WebSocket w/Version */
  public Observable<WebSocket> connectWebsocketRx(String s, WebSocketVersion webSocketVersion) {
    ReplySubject<WebSocket> rx=ReplySubject.create();
    nested.connectWebsocket(s,webSocketVersion,rx);
    return rx;
  }

  /** Fetch URL using simple GET */
  public Observable<HttpClientResponse> getNowRx(String s) {
    ReplySubject<HttpClientResponse> rx=ReplySubject.create();
    nested.getNow(s,rx);
    return rx;
  }

  /** Fetch URL using simple GET w/Params */
  public Observable<HttpClientResponse> getNowRx(String s, Map<String, ? extends Object> stringMap) {
    ReplySubject<HttpClientResponse> rx=ReplySubject.create();
    nested.getNow(s,stringMap,rx);
    return rx;
  }

  /** Construct OPTIONS request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> optionsRx(String s, Action1<HttpClientRequest> builder) {
    return request("OPTIONS",s,builder);
  }

  /** Construct GET request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> getRx(String s, Action1<HttpClientRequest> builder) {
    return request("GET",s,builder);
  }

  /** Construct HEAD request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> headRx(String s, Action1<HttpClientRequest> builder) {
    return request("HEAD",s,builder);
  }

  /** Construct POST request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> postRx(String s, Action1<HttpClientRequest> builder) {
    return request("POST",s,builder);
  }

  /** Construct PUT request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> putRx(String s, Action1<HttpClientRequest> builder) {
    return request("PUT",s,builder);
  }

  /** Construct DELETE request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> deleteRx(String s, Action1<HttpClientRequest> builder) {
    return request("DELETE",s,builder);
  }

  /** Construct TRACE request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> traceRx(String s, Action1<HttpClientRequest> builder) {
    return request("TRACE",s,builder);
  }

  /** Construct CONNECT request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> connectRx(String s, Action1<HttpClientRequest> builder) {
    return request("CONNECT",s,builder);
  }

  /** Construct PATCH request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> patchRx(String s, Action1<HttpClientRequest> builder) {
    return request("PATCH",s,builder);
  }

  /** Construct named request.
   * 
   * @param uri       uri
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<HttpClientResponse> for the response
   * 
   */
  public Observable<HttpClientResponse> request(String method, String uri, Action1<HttpClientRequest> builder) {
    ReplySubject<HttpClientResponse> rx=ReplySubject.create();
    HttpClientRequest req=nested.request(method,uri,rx);
    // Use the builder to create the full request (or start upload)
    // We assume builder will call request.end()
    builder.call(req);
    return rx; 
  }

  // HttpClient implementation
  
  public void exceptionHandler(Handler<Exception> exceptionHandler) {
    nested.exceptionHandler(exceptionHandler);
  }

  public HttpClient setMaxPoolSize(int i) {
    nested.setMaxPoolSize(i);
    return this;
  }

  public int getMaxPoolSize() {
    return nested.getMaxPoolSize();
  }

  public HttpClient setKeepAlive(boolean b) {
    nested.setKeepAlive(b);
    return this;
  }

  public HttpClient setPort(int i) {
    nested.setPort(i);
    return this;
  }

  public HttpClient setHost(String s) {
    nested.setHost(s);
    return this;
  }

  public void connectWebsocket(String s, Handler<WebSocket> webSocketHandler) {
    nested.connectWebsocket(s,webSocketHandler);
  }

  public void connectWebsocket(String s, WebSocketVersion webSocketVersion, Handler<WebSocket> webSocketHandler) {
    nested.connectWebsocket(s,webSocketVersion,webSocketHandler);
  }

  public void getNow(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    nested.getNow(s,httpClientResponseHandler);
  }

  public void getNow(String s, Map<String, ? extends Object> stringMap, Handler<HttpClientResponse> httpClientResponseHandler) {
    nested.getNow(s,stringMap,httpClientResponseHandler);
  }

  public HttpClientRequest options(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.options(s,httpClientResponseHandler);
  }

  public HttpClientRequest get(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.get(s,httpClientResponseHandler);
  }

  public HttpClientRequest head(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.head(s,httpClientResponseHandler);
  }

  public HttpClientRequest post(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.post(s,httpClientResponseHandler);
  }

  public HttpClientRequest put(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.put(s,httpClientResponseHandler);
  }

  public HttpClientRequest delete(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.delete(s,httpClientResponseHandler);
  }

  public HttpClientRequest trace(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.trace(s,httpClientResponseHandler);
  }

  public HttpClientRequest connect(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.connect(s,httpClientResponseHandler);
  }

  public HttpClientRequest patch(String s, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.patch(s,httpClientResponseHandler);
  }

  public HttpClientRequest request(String s, String s1, Handler<HttpClientResponse> httpClientResponseHandler) {
    return nested.request(s,s1,httpClientResponseHandler);
  }

  public void close() {
    nested.close();
  }

  public HttpClient setSSL(boolean b) {
    nested.setSSL(b);
    return this;
  }

  public HttpClient setVerifyHost(boolean b) {
    nested.setVerifyHost(b);
    return this;
  }

  public HttpClient setKeyStorePath(String s) {
    nested.setKeyStorePath(s);
    return this;
  }

  public HttpClient setKeyStorePassword(String s) {
    nested.setKeyStorePassword(s);
    return this;
  }

  public HttpClient setTrustStorePath(String s) {
    nested.setTrustStorePath(s);
    return this;
  }

  public HttpClient setTrustStorePassword(String s) {
    nested.setTrustStorePassword(s);
    return this;
  }

  public HttpClient setTrustAll(boolean b) {
    nested.setTrustAll(b);
    return this;
  }

  public HttpClient setTCPNoDelay(boolean b) {
    nested.setTCPNoDelay(b);
    return this;
  }

  public HttpClient setSendBufferSize(int i) {
    nested.setSendBufferSize(i);
    return this;
  }

  public HttpClient setReceiveBufferSize(int i) {
    nested.setReceiveBufferSize(i);
    return this;
  }

  public HttpClient setTCPKeepAlive(boolean b) {
    nested.setTCPKeepAlive(b);
    return this;
  }

  public HttpClient setReuseAddress(boolean b) {
    nested.setReuseAddress(b);
    return this;
  }

  public HttpClient setSoLinger(boolean b) {
    nested.setSoLinger(b);
    return this;
  }

  public HttpClient setTrafficClass(int i) {
    nested.setTrafficClass(i);
    return this;
  }

  public HttpClient setConnectTimeout(long l) {
    nested.setConnectTimeout(l);
    return this;
  }

  public HttpClient setBossThreads(int i) {
    nested.setBossThreads(i);
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

  public Long getConnectTimeout() {
    return nested.getConnectTimeout();
  }

  public Integer getBossThreads() {
    return nested.getBossThreads();
  }

  public boolean isSSL() {
    return nested.isSSL();
  }

  public boolean isVerifyHost() {
    return nested.isVerifyHost();
  }

  public boolean isTrustAll() {
    return nested.isTrustAll();
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
