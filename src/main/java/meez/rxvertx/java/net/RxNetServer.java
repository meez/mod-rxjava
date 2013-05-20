package meez.rxvertx.java.net;

import meez.rxvertx.java.impl.ResultMemoizeHandler;
import meez.rxvertx.java.impl.SingleObserverHandler;
import org.vertx.java.core.net.NetServer;
import org.vertx.java.core.net.NetSocket;
import rx.Observable;

/** RxNetServer */
public class RxNetServer {
  
  private final NetServer core;
  
  public RxNetServer(NetServer core) {
    this.core=core;
  }
  
  public NetServer coreNetServer() {
    return this.core;
  }
  
  /** Close */
  public Observable<Void> close() {
    ResultMemoizeHandler<Void> rh=new ResultMemoizeHandler<Void>();
    this.core.close(rh);
    return Observable.create(rh.subscribe);
  }

  /** Return an Observable<RxNetSocket> */
  public Observable<RxNetSocket> connect() {
    return Observable.create(
      new SingleObserverHandler<RxNetSocket,NetSocket>() {
          public void register() {
            core.connectHandler(this);
          }
          public void clear() {
            core.connectHandler(null);
          }
          public RxNetSocket wrap(NetSocket s) {
            return new RxNetSocket(s);
          }
        }.subscribe      
    );
  }
}
