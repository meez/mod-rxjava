package meez.rxvertx.java.pipeline;

import org.vertx.java.core.Handler;
import rx.Observable;

/** HandlerPipeline */
public abstract class HandlerPipeline<T,R> implements Handler<T> {

  // Processing
  
  /** Raw msg */
  public abstract Observable<R> process(T msg);
 
  /** Send reply */
  public abstract void sendReply(final T src, final Observable<R> resp);

  // Handler implementation
  
  public void handle(T msg) { 
    Observable<R> res;
    try {
      res=process(msg);
    }
    catch(Exception e) {
      res=Observable.error(e);
    }
    sendReply(msg,res);
  }
}
