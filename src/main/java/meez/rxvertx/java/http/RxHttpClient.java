package meez.rxvertx.java.http;

import meez.rxvertx.java.subject.ReplySubject;
import meez.rxvertx.java.subject.StreamSubject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.*;
import rx.Observable;
import rx.util.functions.Action1;

import java.util.Map;

/** RxWrapper for HttpClient */
public class RxHttpClient {
  
  /** Nested */
  private final HttpClient nested;
  
  /** Create new RxHttpClient */
  public RxHttpClient(HttpClient nested) {
    this.nested=nested;
  }
  
  /** Return core */
  public HttpClient coreHttpClient() {
    return this.nested;
  }

  /** Convenience wrapper */
  public void close() {
    this.nested.close();
  }
  
  // Rx extensions
  
  /** General exception handler */
  public Observable<Exception> exceptionHandler() {
    StreamSubject<Exception> rx=StreamSubject.create();
    nested.exceptionHandler(rx);
    return rx;
  }

  /** Connect to WebSocket */
  public Observable<WebSocket> connectWebsocket(String s) {
    ReplySubject<WebSocket> rx=ReplySubject.create();
    nested.connectWebsocket(s,rx);
    return rx;
  }

  /** Connect to WebSocket w/Version */
  public Observable<WebSocket> connectWebsocket(String s, WebSocketVersion webSocketVersion) {
    ReplySubject<WebSocket> rx=ReplySubject.create();
    nested.connectWebsocket(s,webSocketVersion,rx);
    return rx;
  }

  /** Fetch URL using simple GET */
  public Observable<RxHttpClientResponse> getNow(String s) {
    final ReplySubject<RxHttpClientResponse> rx=ReplySubject.create();
    nested.getNow(s,new Handler<HttpClientResponse>() {
      public void handle(HttpClientResponse resp) {
        rx.handle(new RxHttpClientResponse(resp));
      }
    });
    return rx;
  }

  /** Fetch URL using simple GET w/Params */
  public Observable<RxHttpClientResponse> getNow(String s, Map<String, ? extends Object> params) {
    final ReplySubject<RxHttpClientResponse> rx=ReplySubject.create();
    nested.getNow(s,params,mapRx(rx));
    return rx;
  }

  /** Construct OPTIONS request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> options(String s, Action1<HttpClientRequest> builder) {
    return request("OPTIONS",s,builder);
  }

  /** Construct GET request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> get(String s, Action1<HttpClientRequest> builder) {
    return request("GET",s,builder);
  }

  /** Construct HEAD request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> head(String s, Action1<HttpClientRequest> builder) {
    return request("HEAD",s,builder);
  }

  /** Construct POST request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> post(String s, Action1<HttpClientRequest> builder) {
    return request("POST",s,builder);
  }

  /** Construct PUT request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> put(String s, Action1<HttpClientRequest> builder) {
    return request("PUT",s,builder);
  }

  /** Construct DELETE request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> delete(String s, Action1<HttpClientRequest> builder) {
    return request("DELETE",s,builder);
  }

  /** Construct TRACE request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> trace(String s, Action1<HttpClientRequest> builder) {
    return request("TRACE",s,builder);
  }

  /** Construct CONNECT request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> connect(String s, Action1<HttpClientRequest> builder) {
    return request("CONNECT",s,builder);
  }

  /** Construct PATCH request.
   * 
   * @param s         url
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> patch(String s, Action1<HttpClientRequest> builder) {
    return request("PATCH",s,builder);
  }

  /** Construct named request.
   * 
   * @param uri       uri
   * @param builder   Action1<HttpClientRequest> used to construct the upload
   * @return Observable<RxHttpClientResponse> for the response
   * 
   */
  public Observable<RxHttpClientResponse> request(String method, String uri, Action1<HttpClientRequest> builder) {
    ReplySubject<RxHttpClientResponse> rx=ReplySubject.create();
    HttpClientRequest req=nested.request(method,uri,mapRx(rx));
    // Use the builder to create the full request (or start upload)
    // We assume builder will call request.end()
    builder.call(req);
    return rx; 
  }

  // Implementation
  
  private Handler<HttpClientResponse> mapRx(final ReplySubject<RxHttpClientResponse> rx) {
    return new Handler<HttpClientResponse>() {
      public void handle(HttpClientResponse resp) {
        rx.handle(new RxHttpClientResponse(resp));
      }
    };
  }
}
