package meez.rxvertx.java.pipeline;

import meez.rxvertx.java.RxException;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import rx.Observable;
import rx.functions.Action1;

/** Pipeline for handling BusMod requests */
public class EventBusPipeline<T> extends HandlerPipeline<Message<T>,Message<T>,T> {
  
  private final static Logger log=LoggerFactory.getLogger(EventBusPipeline.class);
  
  // Processing
  
  /** Override to process the raw message */
  public Observable<T> process(Message<T> msg) {
    return processRequest(msg.body);
  }

  /** Override to process the request */
  public Observable<T> processRequest(T value) {
    return processRequest(Observable.just(value));
  }

  /** Body */
  public Observable<T> processRequest(Observable<T> value) {
    throw new RxException("Not implemented");
  }

  // Processing
  
  /** Send reply */
  public void sendReply(final Observable<T> resp, final Message<T> src) {
    resp.subscribe(renderValue(src),renderError(src));
  }
  
  /** Return value renderer */
  public Action1<T> renderValue(final Message<T> src) {
    return new Action1<T>() {
      public void call(T value) {
        src.reply(value);
      }
    };
  }
  
  /** Return error renderer */
  public Action1<Throwable> renderError(final Message<T> src) {
    return new Action1<Throwable>() {
      public void call(Throwable e) {
        // There is no standard way to send errors to non-JsonObject messages. Log and ignore for now
        log.warn("EventBus handler failed (req="+src+")",e);
      }
    };
  }

  // Internal 
  
  @Override
  protected Message<T> wrap(Message<T> in) {
    return in;
  }

  // Handler implementation

  /** Handle message */
  public void handle(Message<T> msg) {
    Observable<T> res;
    try {
      res=process(msg);
    }
    catch(Exception e) {
      res=Observable.error(e);
    }
    sendReply(res,msg);
  }
}
