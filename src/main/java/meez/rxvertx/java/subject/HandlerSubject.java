package meez.rxvertx.java.subject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import rx.util.AtomicObservableSubscription;
import rx.util.functions.Func1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** Base class HandlerSubject.
 * 
 * <p>Implements a Subject that also implements Handler. It will keep only the first result or error received and 
 * send it to all subscribers (incl. future subscribers) exactly once.</p> 
 *
 **/
public class HandlerSubject<T> extends Observable<T> {

  /** Real subscription handler */
  protected class SubscriptionFunc implements Func1<Observer<T>, Subscription> {
    @Override
    public Subscription call(final Observer<T> observer) {
      synchronized(observers) {
        // Check existing state
        switch(state) {
          
          // Already complete
          case COMPLETE:
            observer.onNext(result);
            observer.onCompleted();
            return Subscriptions.empty();
          
          // Already failed
          case FAILED:
            observer.onError(error);
            return Subscriptions.empty();
          
          default:
            break;
        }
        
        final AtomicObservableSubscription subscription=new AtomicObservableSubscription();
        subscription.wrap(new Subscription() {
          public void unsubscribe() {
            observers.remove(subscription);
          }
        });
        observers.put(subscription, observer);
        return subscription;
      }
    }
  }
  
  enum State { READY, COMPLETE, FAILED };
  
private final String id;  
  
  private final ConcurrentHashMap<Subscription, Observer<T>> observers=new ConcurrentHashMap<Subscription,Observer<T>>();
  private State state;
  private T result;
  private Exception error;

  /** Create new HandlerSubject */
  protected HandlerSubject(SubjectSupport.DelegateSubscriptionFunc<T> fnDelegate) {
    super(fnDelegate);
    // Once super() has been called we can reveal the real subscription func 
    fnDelegate.wrap(new SubscriptionFunc());
    
this.id=toString();    
    
    this.state=State.READY;
    this.result=null;
    this.error=null;
  }
  
  // Callbacks
  
  protected void onHandlerCompleted(T value) {
    synchronized(observers) {
      this.state=State.COMPLETE;
      this.result=value;
    }
    // Notify remaining observers (there will be no new observers)
    List<Observer<T>> obs=new ArrayList<Observer<T>>(observers.values());
    for (Observer<T> observer : obs) {
      observer.onNext(value);
    }
    for (Observer<T> observer : obs) {
      observer.onCompleted();
    }
    observers.clear();
  }
  
  protected void onHandlerFailed(Exception e) {
    // Record state for new observers
    synchronized(observers) {
      state=State.FAILED;
      error=e;
    }
    // Notify remaining observers (there will be no new observers)
    for (Observer<T> observer : new ArrayList<Observer<T>>(observers.values())) {
      observer.onError(e);
    }
    observers.clear();
  }
}
