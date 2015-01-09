package meez.rxvertx.java.impl;

import java.util.concurrent.atomic.AtomicReference;

import org.vertx.java.core.Handler;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/** Mapping from Handler to Observer that supports a single subscription and wrapping of the response object.
 *
 * <p>Sub-classes must implement register() to attach the Handler to the relevant callback. This will only happen
 * once the subscription is made</p> 
 * 
 **/
public abstract class SingleObserverHandler<R, T> implements Handler<T> {

  /** Reference to active observer */
  private AtomicReference<Subscriber<? super R>> obRef=new AtomicReference<Subscriber<? super R>>();

  /** Subscription function */
  public Observable.OnSubscribe<R> subscribe = new Observable.OnSubscribe<R>() {
  	@Override
	public void call(Subscriber<? super R> subscriber) {
	  if (!obRef.compareAndSet(null, subscriber))
	    throw new IllegalStateException("Cannot have multiple subscriptions");
	  register();
	  subscriber.add(Subscriptions.create(unsubscribe));
    }
  };

  /** Unsubscribe action */
  public Action0 unsubscribe = new Action0() {
    public void call() {
      Observer<? super R> ob=obRef.getAndSet(null);
      if (ob==null)
        throw new IllegalStateException("Unsubscribe without subscribe");
      clear();
      // Unsubscribe triggers completed
      ob.onCompleted();
    }
  };

  /** Override to register handler */
  public abstract void register();

  /** Override to clear handler */
  public void clear() {
  }

  /** Override to wrap value */
  @SuppressWarnings("unchecked")
  public R wrap(T value) {
    return (R)value;
  }
  
  /** Complete the handler */
  public void complete() {

    Observer<? super R> ob=obRef.get();
    // Ignore if no active observer
    if (ob==null)
      return;

    ob.onCompleted();

    // Clear the observer ref, there is no next/completed/unsubscribe to follow
    obRef.set(null);
  }
  
  /** Fail the handler - used to handle errors before the handler is called */
  public void fail(Exception e) {
    
    Observer<? super R> ob=obRef.get();
    // Ignore if no active observer
    if (ob==null)
      return;
    
    ob.onError(e);

    // Clear the observer ref, there is no next/completed/unsubscribe to follow
    obRef.set(null);
  }

  // Handler implementation

  public void handle(T value) {
    
    Observer<? super R> ob=obRef.get();
    // Ignore if no active observer
    if (ob==null)
      return;

    try {
      ob.onNext(wrap(value));
    }
    catch (Exception e) {
      // onNext should catch any error related to an individual message and
      // avoid killing the Observable. If it doesnt then we propogate the problem 
      // and close the observable

      ob.onError(e);

      // Clear the observer ref - we are done
      obRef.set(null);
    }
  }
}
