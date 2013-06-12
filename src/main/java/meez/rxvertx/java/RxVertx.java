package meez.rxvertx.java;

import meez.rxvertx.java.http.RxHttpClient;
import meez.rxvertx.java.http.RxHttpServer;
import meez.rxvertx.java.io.RxFileSystem;
import meez.rxvertx.java.net.RxNetClient;
import meez.rxvertx.java.net.RxNetServer;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.shareddata.SharedData;

/** Rx wrapper for Vertx */
public class RxVertx {
  
  private final Vertx nested;
  
  private final RxEventBus eventBus;
  private final RxFileSystem fileSystem;
  private final RxTimer timer;
  
  /** Create new RxVertx wrapping a VertX instance */
  public RxVertx(Vertx vertx) {
    this.nested=vertx;
    
    this.eventBus=new RxEventBus(vertx.eventBus());
    this.fileSystem=new RxFileSystem(vertx.fileSystem());
    this.timer=new RxTimer(vertx);
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
  
  /** Http Server */
  public RxHttpServer createHttpServer() {
    return new RxHttpServer(nested.createHttpServer());
  }
  
  /** Http Client */
  public RxHttpClient createHttpClient() {
    return new RxHttpClient(nested.createHttpClient());
  }
  
  /** Net Server */
  public RxNetServer createNetServer() {
    return new RxNetServer(nested.createNetServer());
  }

  /** Net Client */
  public RxNetClient createNetClient() {
    return new RxNetClient(nested.createNetClient());
  }

  /** Return SharedData */
  public SharedData sharedData() {
    return nested.sharedData();
  }
  
  /** Return timer wrapper */
  public RxTimer timer() {
    return timer;
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
