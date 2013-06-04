package meez.rxvertx.java;

import meez.rxvertx.java.impl.MemoizeHandler;
import meez.rxvertx.java.impl.SingleObserverHandler;
import org.vertx.java.core.Vertx;
import rx.Observable;

/** RxTimer */
public class RxTimer {
  
  private Vertx core;

  public RxTimer(Vertx vertx) {
    this.core=vertx;
  }
  
  /** One-off timer */
  public <T> Observable<T> after(final long delay, final T msg) {
    // Map the result to msg
    final MemoizeHandler<T,Long> rh=new MemoizeHandler<T,Long>() {
      @Override public void handle(Long value) {
        // Map the result 
        complete(msg);
      }
    };
    // NB Timer will still fire even if we unsubscribe before it completes. It will just be ignored
    core.setTimer(delay,rh);
    return Observable.create(rh.subscribe);
  } 
  
  /** Repeated timer */
  public <T> Observable<T> every(final long delay, final T msg) {
    // Subscribe on demand
    return Observable.create(
      new SingleObserverHandler<T,Long>() {
        long tid;
        public void register() {
          tid=core.setPeriodic(delay,this);
        }
        public void clear() {
          core.cancelTimer(tid);
        }
      
        public T wrap(Long id) {
          return msg;
        }
        }.subscribe
    );
  }
  
}
