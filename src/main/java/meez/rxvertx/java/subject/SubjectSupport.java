package meez.rxvertx.java.subject;

import java.util.concurrent.ConcurrentHashMap;

import rx.Observer;
import rx.Subscription;
import rx.util.AtomicObservableSubscription;
import rx.util.functions.Func1;

/** SubjectSupport */
public class SubjectSupport {

  /** In order to get around issues with passing inner classes to constructors, this DelegateSubscriptionFunc is used 
   * to wrap the real subscription function once the constructor has executed.
   * 
   * <p>Based on the same pattern in ReplySubject, not that that constitutes a reasonable defence</p>
   * 
   * @see <a href="https://github.com/Netflix/RxJava/blob/master/rxjava-core/src/main/java/rx/subjects/ReplaySubject.java">java.rx.subjects.ReplySubject</a>
   * 
   */
  public static final class DelegateSubscriptionFunc<T> implements Func1<Observer<T>, Subscription>
  {
      private Func1<Observer<T>, Subscription> delegate = null;

      public void wrap(Func1<Observer<T>, Subscription> delegate)
      {
          if (this.delegate != null) {
              throw new UnsupportedOperationException("delegate already set");
          }
          this.delegate=delegate;
      }

      @Override
      public Subscription call(Observer<T> observer)
      {
          return delegate.call(observer);
      }
  }
  
  /** Create a boiler-plate subscriber for a given object 
   * 
   * <p>Based on PublishSubject.create</p> 
   * 
   * @see <a href="https://github.com/Netflix/RxJava/blob/master/rxjava-core/src/main/java/rx/subjects/PublishSubject.java">java.rx.subjects.PublishSubject</a>
   * 
   **/
  public static <T> Func1<Observer<T>,Subscription> mkSubscriber(final ConcurrentHashMap<Subscription, Observer<T>> map) {
    return new Func1<Observer<T>, Subscription>() {
      public Subscription call(Observer<T> observer) {
        
        final AtomicObservableSubscription subscription=new AtomicObservableSubscription();
    
        subscription.wrap(new Subscription() {
          public void unsubscribe() {
            // on unsubscribe remove it from the map of outbound observers to notify
            map.remove(subscription);
          }
        });
    
        // on subscribe add it to the map of outbound observers to notify
        map.put(subscription, observer);
        return subscription;
      }    
    };
  }
}
