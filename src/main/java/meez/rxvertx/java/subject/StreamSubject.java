package meez.rxvertx.java.subject;

import org.vertx.java.core.Handler;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.util.functions.Func1;

import java.util.concurrent.ConcurrentHashMap;

/** StreamSubject 
 * 
 * <p>
 * Subject used to handle a stream of messages. onNext called for each Message for only active subscribers. onCompleted 
 * called when stream completes. No replay of events. 
 * </p>
 *  
 **/
public class StreamSubject<T> extends PublishSubject<T> implements Handler<T> {
  
  /** Create */
  public static <T> StreamSubject<T> create() {
    final ConcurrentHashMap<Subscription, Observer<T>> observers=new ConcurrentHashMap<Subscription, Observer<T>>();
    return new StreamSubject<T>(SubjectSupport.mkSubscriber(observers),observers);
  }
  
  protected StreamSubject(Func1<Observer<T>, Subscription> onSubscribe, ConcurrentHashMap<Subscription, Observer<T>> observers) {
    super(onSubscribe,observers);
  }
  
  /** Return a completion handler */
  public Handler<Void> completionHandler() {
    return new Handler<Void>() {
      public void handle(Void aVoid) {
        onCompleted();
      }
    };
  }
  
  /** Return an exception handler */
  public Handler<Exception> exceptionHandler() {
    return new Handler<Exception>() {
      public void handle(Exception e) {
        onError(e);
      }
    };
  }
  
  // Handler implementation

  public void handle(T value) {
    onNext(value);
  }
}
