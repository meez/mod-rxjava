package meez.rxvertx.java.subject;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;

/** AsyncResultSubject 
 * 
 * <p>
 * Subject used to handle a single AsyncResultHandler reply. onNext & onCompleted are dispatched to all subscribers when
 * the handler completes 
 * </p>
 *  
 **/
public class AsyncResultSubject<T> extends HandlerSubject<T> implements AsyncResultHandler<T> {

  /** Create new AsyncResultSubject */
  public static <T> AsyncResultSubject<T> create() {
    return new AsyncResultSubject<T>();
  }
  
  protected AsyncResultSubject() {
    super(new SubjectSupport.DelegateSubscriptionFunc<T>());
  }

  // AsyncResultHandler implementation
  
  /** Single response completes subject for all subscribers */
  public void handle(AsyncResult<T> value) {
    if (value.succeeded()) {
      onHandlerCompleted(value.result);
    }
    else {
      onHandlerFailed(value.exception);
    }
  }
}
