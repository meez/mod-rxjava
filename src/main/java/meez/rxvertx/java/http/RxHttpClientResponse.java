package meez.rxvertx.java.http;

import meez.rxvertx.java.RxSupport;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClientResponse;
import rx.Observable;

import java.util.List;
import java.util.Map;

/** Rx wrapper for HttpClientResponse
 * 
 * <p>Replace *Handler methods with asObservable*</p>
 *  
 **/
public class RxHttpClientResponse extends HttpClientResponse {
  
  /** Real response */
  private final HttpClientResponse nested;
  
  /** Create new HttpClientResponse */
  protected RxHttpClientResponse(HttpClientResponse real) {
    super(real.statusCode,real.statusMessage);
    this.nested=real;
  }
  
  /** Return observable for accessing the response as a stream of Buffer */
  public Observable<Buffer> asObservable() {
    return RxSupport.toObservable(nested);
  }
  
  // HttpClientResponse implementation
  
  @Override
  public Map<String, String> headers() {
    return nested.headers();
  }

  @Override
  public Map<String, String> trailers() {
    return nested.trailers();
  }

  @Override
  public List<String> cookies() {
    return nested.cookies();
  }
  
  // HttpReadStreamBase implementation
  
  public void bodyHandler(Handler<org.vertx.java.core.buffer.Buffer> bodyHandler) {
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
