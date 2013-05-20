package meez.rxvertx.java.http;

import meez.rxvertx.java.RxSupport;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import rx.Observable;

import java.util.Map;

/** Rx wrapper for HttpServerRequest 
 * 
 * <p>Replace *Handler methods with asObservable*</p>
 *  
 **/
public class RxHttpServerRequest extends HttpServerRequest {
  
  /** Real instance */
  private HttpServerRequest nested;

  /** Create new RxHttpServerRequest */
  protected RxHttpServerRequest(HttpServerRequest real) {
    super(real.method, real.uri, real.path, real.query, real.response);
    this.nested=real;
  }

  /** Return observable for accessing the response as a stream of Buffer */
  public Observable<Buffer> asObservable() {
    return RxSupport.toObservable(nested);
  }
  
  // HttpServerRequest implementation 
  
  @Override
  public Map<String, String> headers() {
    return nested.headers();
  }

  @Override
  public Map<String, String> params() {
    return nested.params();
  }
  
  // HttpReadStreamBase implementation
  
  @Override 
  public void bodyHandler(Handler<Buffer> bodyHandler) {
    throw new UnsupportedOperationException("Not supported in Rx - use asObservable");
  }  
  
  // ReadStream implementation

  @Override
  public void dataHandler(Handler<Buffer> bufferHandler) {
    throw new UnsupportedOperationException("Not supported in Rx - use asObservable");
  }

  @Override
  public void pause() {
    nested.pause();
  }

  @Override
  public void resume() {
    nested.resume();
  }

  @Override
  public void exceptionHandler(Handler<Exception> exceptionHandler) {
    throw new UnsupportedOperationException("Not supported in Rx - use asObservable");
  }

  @Override
  public void endHandler(Handler<Void> voidHandler) {
    throw new UnsupportedOperationException("Not supported in Rx - use asObservable");
  }
}
