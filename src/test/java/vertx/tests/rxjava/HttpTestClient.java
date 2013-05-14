package vertx.tests.rxjava;

import java.io.UnsupportedEncodingException;

import meez.rxvertx.java.RxSupport;
import meez.rxvertx.java.RxVertx;
import meez.rxvertx.java.http.RxHttpClient;
import meez.rxvertx.java.http.RxHttpSupport;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.testframework.TestClientBase;
import rx.util.functions.Action0;
import rx.util.functions.Action1;

/** HttpTestClient */
public class HttpTestClient extends TestClientBase {
  
  private RxHttpClient client;

  @Override
  public void start() {
    super.start();
    tu.appReady();
    client=new RxVertx(vertx).createHttpClient();
    client.setHost("localhost").setPort(8080);
  }

  @Override
  public void stop() {
    client.close();
    super.stop();
  }

  public Action0 testComplete() {
    return new Action0() {
      public void call() {
        tu.testComplete();
      }
    };
  }
  
  public Action1<Exception> testFailed() {
    return new Action1<Exception>() {
      public void call(Exception e) {
        tu.azzert(false);
      }
    };
  }
  
  // Tests
  
  public void testPostJsonRx() throws UnsupportedEncodingException {

    final JsonObject jsonReq=new JsonObject().putString("msg","ping");

    client
      // POST JsonObject
      .postRx("/ping",RxHttpSupport.uploadJson(jsonReq,"UTF-8"))
      // Download body
      .flatMap(RxHttpSupport.downloadBody())
      // Decode response
      .map(RxSupport.decodeJson("UTF-8"))
      // Validate
      .subscribe(new Action1<JsonObject>() {
        public void call(JsonObject json) {
          tu.azzert("pong".equals(json.getString("msg")));
          tu.azzert("ping".equals(json.getString("src")));
          tu.testComplete();
        }
      },testFailed());
  }
}
