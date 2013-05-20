package meez.rxvertx.java.net;

import meez.rxvertx.java.impl.SingleObserverHandler;
import org.vertx.java.core.net.NetClient;
import org.vertx.java.core.net.NetSocket;
import rx.Observable;

/** RxNetClient */
public class RxNetClient {
  
  /** Core client */
  private final NetClient core;

  /** Create new RxNetClient */
  public RxNetClient(NetClient core) {
    this.core=core;
  }

  /** Connect to the given port 
   * 
   * @param   port    Port to connect to 
   * @return  Observable<RxNetSocket> that calls onNext once when the connection is established or onError if it fails 
   * 
   */
  public Observable<RxNetSocket> connect(final int port) {
    return Observable.create(
      new SingleObserverHandler<RxNetSocket,NetSocket>() {
          public void register() {
            core.connect(port,this);
          }
          public RxNetSocket wrap(NetSocket s) {
            return new RxNetSocket(s);
          }
        }.subscribe      
    );
  }
  
  public Observable<RxNetSocket> connect(final int port, final String host) {
    return Observable.create(
      new SingleObserverHandler<RxNetSocket,NetSocket>() {
          public void register() {
            core.connect(port,host,this);
          }
          public RxNetSocket wrap(NetSocket s) {
            return new RxNetSocket(s);
          }
        }.subscribe      
    );
  }
  
  public void close() {
    core.close();
  }
}
