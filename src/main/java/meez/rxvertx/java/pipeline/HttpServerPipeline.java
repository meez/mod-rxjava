package meez.rxvertx.java.pipeline;

import meez.rxvertx.java.RxException;
import meez.rxvertx.java.http.RxHttpServerRequest;
import meez.rxvertx.java.http.RxHttpSupport;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import rx.Observable;
import rx.util.functions.Action0;
import rx.util.functions.Action1;

/** Pipeline for handling HttpServer requests */
public class HttpServerPipeline<T> extends HandlerPipeline<RxHttpServerRequest, HttpServerRequest, T> {
  
  // Processing
  
  /** Main request processor */
  public Observable<T> process(RxHttpServerRequest request) {
    // Wrap request in Observable
    return process(Observable.just(request));
  }

  /** Main request processor */
  public Observable<T> process(Observable<RxHttpServerRequest> request) {
    throw new RxException("Not implemented");
  }
  
  // Rendering
  
  /** Send reply */
  public void sendReply(final Observable<T> resp, final RxHttpServerRequest req) {
    resp.subscribe(renderValue(req),renderError(req),renderComplete(req));
  }

  /** Return value renderer */
  public <V> Action1<V> renderValue(final HttpServerRequest src) {
    return new Action1<V>() {
      // Basic string/json renderer. Override to create proper encoder / objectmapper
      public void call(Object o) {
        src.response.setChunked(true);
        if (o instanceof String) {
          src.response.write((String)o,"utf8");
        }
        else if (o instanceof JsonObject) {
          JsonObject jo=(JsonObject)o;
          src.response.write(jo.encode(),"utf8");
        }
        else {
          src.response.statusCode=406;
          src.response.statusMessage="Unable to encode type";
          src.response.write("unable to encode type");
        }
      }
    };
  }
  
  /** Return error renderer */
  public Action1<Exception> renderError(final HttpServerRequest src) {
    return new Action1<Exception>() {
      public void call(Exception e) {
        src.response.statusCode=500;
        src.response.statusMessage="Request failed: "+e;
        src.response.putHeader("Content-type","text/plain");
        src.response.end("Request failed\n"+e);
      }
    };
  }

  /** Return completion renderer */
  private Action0 renderComplete(final HttpServerRequest src) {
    return new Action0() {
      public void call() {
        src.response.end();
      }
    };
  }

  // Utility

  /** Wrapper */
  protected RxHttpServerRequest wrap(HttpServerRequest req) {
    return RxHttpSupport.asRx(req);
  }

  /** Return json */
  protected Observable<JsonObject> json(JsonObject msg) {
    return Observable.just(msg);
  }

  /** Return text */
  protected Observable<String> text(String msg) {
    return Observable.just(msg);
  }
}
