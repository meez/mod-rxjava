package meez.rxvertx.java.impl;

import java.util.concurrent.atomic.AtomicReference;

import org.vertx.java.core.Handler;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/** Subject that stores the result of a Handler and notfies all current and future Observers */
public class MemoizeHandler<R,T> implements Handler<T> {
  
  /** States */
  enum State { ACTIVE, COMPLETED, FAILED }
  
  /** State */
  private State state;
  
  /** Result */
  private R result;
  
  /** Error */
  private Exception error;
  
  /** Reference to active observer */
  private AtomicReference<Subscriber<?super R>> obRef=new AtomicReference<Subscriber<? super R>>();
  
  /** Create new MemoizeHandler */
  public MemoizeHandler() {
    this.state=State.ACTIVE;
    this.result=null;
    this.error=null;
  }
  
  /** Subscription function */
	public Observable.OnSubscribe<R> subscribe = new Observable.OnSubscribe<R>() {
		@Override
		public void call(Subscriber<? super R> newObserver) {
			// Check if complete
			switch(state) {

			// Completed. Forward the saved result
			case COMPLETED:
				dispatchResult(newObserver,result);
				newObserver.add(Subscriptions.empty());
				return;

			// Failed already. Forward the saved error
			case FAILED:
				dispatchError(newObserver,error);
				newObserver.add(Subscriptions.empty());
				return;
			}

			// State=ACTIVE
			if (!obRef.compareAndSet(null, newObserver))
				throw new IllegalStateException("Cannot have multiple subscriptions");

			newObserver.add(Subscriptions.create(unsubscribe));
		}
	};

  /** Unsubscribe action */
  public Action0 unsubscribe=new Action0() {
    public void call() {
		Subscriber<? super R> ob=obRef.getAndSet(null);
      if (ob==null)
        throw new IllegalStateException("Unsubscribe without subscribe");
    }
  };

  /** Dispatch complete */
  public void complete(R value) {
    this.result=value;
    this.state=State.COMPLETED;

	  Subscriber<? super R> ob=obRef.get();
    // Ignore if no active observer
    if (ob==null)
      return;

    dispatchResult(ob,value);
  }
  
  /** Dispatch failure */
  public void fail(Exception e) {
    this.error=e;
    this.state=State.FAILED;

	  Subscriber<? super R> ob=obRef.get();
    // Ignore if no active observer
    if (ob==null)
      return;

    dispatchError(ob,e);
  }
  
  // Handler implementation
  
  /** Complete */
  @SuppressWarnings("unchecked")
  public void handle(T value) {
    // Default: Assume same type
    complete((R)value);
  }
  
  // Implementation
  
  /** Dispatch result */
  private void dispatchResult(Subscriber<? super R> ob, R value) {
    try {
      ob.onNext(value);
    }
    catch(Throwable t) {
      dispatchError(ob,new RuntimeException("Observer call failed (e="+t+")",t));
      return;
    }

    // We do not retry onError() once onCompleted() has been attempted. For now we just log the error and continue.
    // Unsure as to what the best approach is here
    try {
      ob.onCompleted();
    }
    catch(Throwable t) {
      // FIXME: Logging
      System.err.println("dispatchResult("+value+") onCompleted() has failed (e="+t+")");
      t.printStackTrace(System.err);
    }
  }
  
  /** Dispatch error */
  private void dispatchError(Observer ob, Exception ex) {
    try {
      ob.onError(ex);
    }
    catch(Throwable t) {
      // FIXME: Logging
      System.err.println("dispatchErrror("+ex+") failed. (e="+t+")");
      t.printStackTrace(System.err);
    }
  }
}
