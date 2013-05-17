package meez.rxvertx.java;

import meez.rxvertx.java.io.RxFileSystem;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.shareddata.SharedData;

/** Rx wrapper for Vertx */
public class RxVertx {
  
  private final Vertx nested;
  
  private final RxEventBus eventBus;
  private final RxFileSystem fileSystem;
  
  /** Create new RxVertx wrapping a VertX instance */
  public RxVertx(Vertx vertx) {
    this.nested=vertx;
    
    this.eventBus=new RxEventBus(vertx.eventBus());
    this.fileSystem=new RxFileSystem(vertx.fileSystem());
  }
  
  // Vertx implementation

  /** Return Rx extended FileSystem */
  public RxFileSystem fileSystem() {
    return fileSystem;
  }
  
  /** Return Rx extended EventBus */
  public RxEventBus eventBus() {
    return eventBus;
  }
  
  /** Return SharedData */
  public SharedData sharedData() {
    return nested.sharedData();
  }
  
  // Timers
  // Need a proper rx timer 
  // Observable<Long> setTimer(long delay)
  // Observable<Long> setPeriodic(long period)
  // void cancelTimer(Observable<Long> subject)
  
  /** Set a single timer */
  public long setTimer(long l, Handler<Long> handler) {
    return nested.setTimer(l,handler);
  }
  
  /** Set a periodic timer */
  public long setPeriodic(long l, Handler<Long> handler) {
    return nested.setPeriodic(l,handler);
  }
  
  /** Cancel a timer 
   *
   * If the timer was created using setTimer or setPeriodic then the Subject may never complete because
   * there is no handler for timer cancellation currently. We could build a local map to implement or else would
   * need a core extension to setTimer(l,handler,cancelHandler) / setPeriodic(l,handler,cancelHandler) 
   * 
   **/
  public boolean cancelTimer(long l) {
    return nested.cancelTimer(l);
  }

  public void runOnLoop(Handler<Void> handler) {
    nested.runOnLoop(handler);
  }
  
  public boolean isEventLoop() {
    return nested.isEventLoop();
  }
  
  public boolean isWorker() {
    return nested.isWorker();
  }
  
  public void stop() {
    nested.stop();  
  }  
}
