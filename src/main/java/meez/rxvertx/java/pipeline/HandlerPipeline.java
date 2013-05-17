package meez.rxvertx.java.pipeline;

import org.vertx.java.core.Handler;
import rx.Observable;

/** HandlerPipeline 
 * 
 * <p>Used to build a workflow from a handler that handles its own outpute e.g. a http request</p> 
 * 
 **/
public abstract class HandlerPipeline<R,S,T> implements Handler<S> {

  // Processing
  
  /** Raw msg */
  public abstract Observable<T> process(R msg);
 
  /** Send reply */
  public abstract void sendReply(final Observable<T> resp, final R src);

  /** Map the core Handler<> type to the Rx wrapper */ 
  protected abstract R wrap(S in);
  
  // Handler implementation
  
  public void handle(S msg) { 
    Observable<T> res;
    try {
      res=process(wrap(msg));
    }
    // If an error happens inside process then ensure response is sent
    catch(Exception e) {
      res=Observable.error(e);
    }
    sendReply(res,wrap(msg));
  }
}
