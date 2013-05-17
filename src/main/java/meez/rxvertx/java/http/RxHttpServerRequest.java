package meez.rxvertx.java.http;

import meez.rxvertx.java.subject.ReplySubject;
import meez.rxvertx.java.subject.StreamSubject;
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

  /** Read as stream */
  private StreamSubject<Buffer> stream;
  
  /** Read as body */
  private ReplySubject<Buffer> body;

  /** Create new RxHttpServerRequest */
  protected RxHttpServerRequest(HttpServerRequest real) {
    super(real.method, real.uri, real.path, real.query, real.response);
    this.nested=real;
  }

  /** Return observable for accessing the response as a stream of Buffer */
  public Observable<Buffer> asObservableStream() {
    // Cannot access as a stream and as a body
    if (this.body!=null)
      throw new IllegalStateException("Cannot mix stream and body from same response");
    if (this.stream!=null)
      return this.stream;
    
    this.stream=RxHttpSupport.toStream(nested);
    
    return this.stream;
  }
  
  /** Return observable for accessing the response as a single Buffer */
  public Observable<Buffer> asObservableBody() {
    // Cannot access as a stream and as a body
    if (this.stream!=null)
      throw new IllegalStateException("Cannot mix stream and body from same response");
    if (this.body!=null)
      return this.body;
    
    this.body=RxHttpSupport.toBody(nested);
    
    return this.body;
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
    throw new UnsupportedOperationException("Cannot access bodyHandler() via Rx - use asObservableStream/asObservableBody");
  }  
  
  // ReadStream implementation

  @Override
  public void dataHandler(Handler<Buffer> bufferHandler) {
    throw new UnsupportedOperationException("Cannot access dataHandler() via Rx - use asObservableStream/asObservableBody");
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
    throw new UnsupportedOperationException("Cannot access exceptionHandler() via Rx - use asObservableStream/asObservableBody");
  }

  @Override
  public void endHandler(Handler<Void> voidHandler) {
    throw new UnsupportedOperationException("Cannot access endHandler() via Rx - use asObservableStream/asObservableBody");
  }
}
