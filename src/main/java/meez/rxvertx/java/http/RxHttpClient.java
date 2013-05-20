package meez.rxvertx.java.http;

import meez.rxvertx.java.impl.MemoizeHandler;
import meez.rxvertx.java.impl.ResultMemoizeHandler;
import org.vertx.java.core.http.*;
import rx.Observable;
import rx.util.functions.Action1;

import java.util.Map;

/** RxWrapper for HttpClient 
 * @author <a href="http://github.com/petermd">Peter McDonnell</a>
 */
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
    ResultMemoizeHandler<Exception> rh=new ResultMemoizeHandler<Exception>();
    nested.exceptionHandler(rh);
    return Observable.create(rh.subscribe);
  }

  /** Connect to WebSocket */
  public Observable<WebSocket> connectWebsocket(String s) {
    ResultMemoizeHandler<WebSocket> rh=new ResultMemoizeHandler<WebSocket>();
    nested.connectWebsocket(s,rh);
    return Observable.create(rh.subscribe);
  }

  /** Connect to WebSocket w/Version */
  public Observable<WebSocket> connectWebsocket(String s, WebSocketVersion webSocketVersion) {
    ResultMemoizeHandler<WebSocket> rh=new ResultMemoizeHandler<WebSocket>();
    nested.connectWebsocket(s,webSocketVersion,rh);
    return Observable.create(rh.subscribe);
  }

  /** Fetch URL using simple GET */
  public Observable<RxHttpClientResponse> getNow(String s) {
    final MemoizeHandler<RxHttpClientResponse,HttpClientResponse> rh=new MemoizeHandler<RxHttpClientResponse,HttpClientResponse>() {
      @Override
      public void handle(HttpClientResponse r) {
        complete(new RxHttpClientResponse(r));
      }
    };
    nested.getNow(s,rh);
    return Observable.create(rh.subscribe);
  }

  /** Fetch URL using simple GET w/Params */
  public Observable<RxHttpClientResponse> getNow(String s, Map<String, ? extends Object> params) {
    final MemoizeHandler<RxHttpClientResponse,HttpClientResponse> rh=new MemoizeHandler<RxHttpClientResponse,HttpClientResponse>() {
      @Override
      public void handle(HttpClientResponse r) {
        complete(new RxHttpClientResponse(r));
      }
    };
    nested.getNow(s,params,rh);
    return Observable.create(rh.subscribe);
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
    
    final MemoizeHandler<RxHttpClientResponse,HttpClientResponse> rh=new MemoizeHandler<RxHttpClientResponse,HttpClientResponse>() {
      @Override
      public void handle(HttpClientResponse r) {
        complete(new RxHttpClientResponse(r));
      }
    };
    
    HttpClientRequest req=nested.request(method,uri,rh);
    
    // Use the builder to create the full request (or start upload)
    // We assume builder will call request.end()
    builder.call(req);
    
    return Observable.create(rh.subscribe);
  }
}
