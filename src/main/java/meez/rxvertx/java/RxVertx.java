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
  
  /** Return timer wrapper */
  public RxTimer timer() {
    return new RxTimer(nested);
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
