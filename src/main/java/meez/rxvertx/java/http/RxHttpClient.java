package meez.rxvertx.java.http;

import java.util.Map;

import meez.rxvertx.java.impl.MemoizeHandler;
import meez.rxvertx.java.impl.ResultMemoizeHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.*;
import rx.Observable;
import rx.functions.Action1;

/** RxWrapper for HttpClient 
 * @author <a href="http://github.com/petermd">Peter McDonnell</a>
 */
public class RxHttpClient {
  
  /** Nested */
  private final HttpClient core;
  
  /** Create new RxHttpClient */
  public RxHttpClient(HttpClient nested) {
    this.core =nested;
  }
  
  /** Return core */
  public HttpClient coreHttpClient() {
    return this.core;
  }

  /** Convenience wrapper */
  public void close() {
    this.core.close();
  }
  
  // Rx extensions
  
  /** Connect to WebSocket */
  public Observable<WebSocket> connectWebsocket(String s) {
    ResultMemoizeHandler<WebSocket> rh=new ResultMemoizeHandler<WebSocket>();
    core.connectWebsocket(s,rh);
    return Observable.create(rh.subscribe);
  }

  /** Connect to WebSocket w/Version */
  public Observable<WebSocket> connectWebsocket(String s, WebSocketVersion webSocketVersion) {
    ResultMemoizeHandler<WebSocket> rh=new ResultMemoizeHandler<WebSocket>();
    core.connectWebsocket(s,webSocketVersion,rh);
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
    // Use request() instead of getNow() so we can trap errors
    return request("GET",s,new Action1<HttpClientRequest>() {
      public void call(HttpClientRequest req) {
        req.end();
      }
    });
  }

  /** Fetch URL using simple GET w/Params */
  public Observable<RxHttpClientResponse> getNow(String s, final Map<String, ? extends Object> headers) {
    final MemoizeHandler<RxHttpClientResponse,HttpClientResponse> rh=new MemoizeHandler<RxHttpClientResponse,HttpClientResponse>() {
      @Override
      public void handle(HttpClientResponse r) {
        complete(new RxHttpClientResponse(r));
      }
    };
    // Use request() instead of getNow() so we can trap errors
    return request("GET",s,new Action1<HttpClientRequest>() {
      public void call(HttpClientRequest req) {
        for (Map.Entry<String,? extends Object> me : headers.entrySet()) {
          req.putHeader(me.getKey(),me.getValue());
        }
        req.end();
      }
    });
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
    
    HttpClientRequest req= core.request(method,uri,rh);

    // if req fails, notify observers
    req.exceptionHandler(new Handler<Exception>() {
        @Override
        public void handle(Exception event) {
            rh.fail(event);
        }
    });
    
    // Use the builder to create the full request (or start upload)
    // We assume builder will call request.end()
    try {
        builder.call(req);
    } catch(Exception e) {
        rh.fail(e);
    }

    return Observable.create(rh.subscribe);
  }
}
