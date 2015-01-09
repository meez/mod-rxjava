package vertx.tests.rxjava;

import java.io.UnsupportedEncodingException;

import meez.rxvertx.java.RxSupport;
import meez.rxvertx.java.RxTestSupport;
import meez.rxvertx.java.net.RxNetClient;
import meez.rxvertx.java.net.RxNetServer;
import meez.rxvertx.java.net.RxNetSocket;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.testframework.TestClientBase;
import rx.functions.Action1;

/**
 * NetTestClient
 */
public class NetTestClient extends TestClientBase {
  
  private RxNetClient client;
  private RxNetServer server;

  @Override
  public void start() {
    super.start();
    tu.appReady();
    client=new RxNetClient(vertx.createNetClient());
  }

  @Override
  public void stop() {
    client.close();
    if (server != null) {
      server
        .close()
        .subscribe(new Action1<Void>() {
          public void call(Void v) {
            tu.checkContext();
            NetTestClient.super.stop();
          }
        });
    } else {
      super.stop();
    }
  }

  // Tests
  
  public void testSimpleServer() throws UnsupportedEncodingException {

    server=new RxNetServer(vertx.createNetServer());
    
    server
      .connect()
      .subscribe(new Action1<RxNetSocket>() {
        public void call(RxNetSocket s) {
          s.write("pong");
          s.close();
        }
      });
    
    server.coreNetServer().listen(8100);
    
    client
      .connect(8100)
      .subscribe(
        new Action1<RxNetSocket>() {
          public void call(RxNetSocket s) {
            s.asObservable()
             .reduce(RxSupport.mergeBuffers)
             .subscribe(
               new Action1<Buffer>() {
                 public void call(Buffer b) {
                   System.out.println("client:"+b);
                   tu.azzert("pong".equals(b.toString()));
                 }
               },
               RxTestSupport.testFailed(tu),
               RxTestSupport.testComplete(tu)
             );
          }
        },
        RxTestSupport.testFailed(tu)
      );
  }
}
