package meez.rxvertx.java.subject;

import org.vertx.java.core.Handler;

/** ReplySubject 
 * 
 * <p>
 * Subject used to handle a single Handler reply. onNext & onCompleted are dispatched to all subscribers when
 * the handler completes 
 * </p>
 *  
 **/
public class ReplySubject<T> extends HandlerSubject<T> implements Handler<T> {
	
  // Factory method

  public static <T> ReplySubject<T> create() {
    return new ReplySubject<T>();
  }

  /** Create */
  protected ReplySubject() {
    super(new SubjectSupport.DelegateSubscriptionFunc<T>());
  }
  
  /** Allow manual error */
  public void fail(Exception e) {
    onHandlerFailed(e);
  }
  
  /** Return exception handler */
  public Handler<Exception> exceptionHandler() {
    return new Handler<Exception>() {
      public void handle(Exception e) {
        onHandlerFailed(e);
      }
    };
  }

  // Handler implementation
  
  /** Single response completes subject for all subscribers */
  public void handle(T value) { 
    onHandlerCompleted(value);
  }
}
