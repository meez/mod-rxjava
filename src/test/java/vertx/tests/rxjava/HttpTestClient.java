package vertx.tests.rxjava;

import meez.rxvertx.java.RxTestSupport;
import meez.rxvertx.java.http.RxHttpClient;
import meez.rxvertx.java.http.RxHttpServer;
import meez.rxvertx.java.http.RxHttpServerRequest;
import meez.rxvertx.java.http.RxHttpSupport;
import meez.rxvertx.java.pipeline.HttpServerPipeline;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.testframework.TestClientBase;
import rx.Observable;
import rx.util.functions.Action1;
import rx.util.functions.Func2;

import java.io.UnsupportedEncodingException;

/** HttpTestClient */
public class HttpTestClient extends TestClientBase {
  
  private RxHttpClient client;
  private RxHttpServer server;

  @Override
  public void start() {
    super.start();
    tu.appReady();
    client=new RxHttpClient(vertx.createHttpClient());
    client.coreHttpClient().setHost("localhost").setPort(8080);
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
            HttpTestClient.super.stop();
          }
        });
    } else {
      super.stop();
    }
  }

  private void startPipelineServer(HttpServerPipeline pipeline, int port) {
    server = new RxHttpServer(vertx.createHttpServer());
    server.coreHttpServer().requestHandler(pipeline).listen(port, "localhost");
  }

  /** Simple ping server */
  private class PingServer extends HttpServerPipeline<JsonObject> {
    public Observable<JsonObject> process(final RxHttpServerRequest req) {
      return json(new JsonObject()
        .putString("msg", "pong")
        .putString("path", req.path)
      );
    }
  }

  // Tests
  
  public void testGetNow() throws UnsupportedEncodingException {

    startPipelineServer(new PingServer(),8080);      
    
    client
      // GET JsonObject
      .getNow("/ping")
      // Download body
      .flatMap(RxHttpSupport.downloadJson())
      // Validate
      .subscribe(
        new Action1<JsonObject>() {
          public void call(JsonObject json) {
            tu.azzert("pong".equals(json.getString("msg")));
            tu.testComplete();
          }
        },
        RxTestSupport.testFailed(tu));
  }

  public void testPostJson() throws UnsupportedEncodingException {

    final JsonObject jsonReq=new JsonObject().putString("msg","ping");

    client
      // POST JsonObject
      .post("/ping",RxHttpSupport.uploadJson(jsonReq))
      // Download body
      .flatMap(RxHttpSupport.downloadJson())
      // Validate
      .subscribe(
        new Action1<JsonObject>() {
          public void call(JsonObject json) {
            tu.azzert("pong".equals(json.getString("msg")));
            tu.azzert("ping".equals(json.getString("src")));
            tu.testComplete();
          }
        },
        RxTestSupport.testFailed(tu));
  }
  
  public void testParallelUpload() throws UnsupportedEncodingException {
      
    // Create two async Json requests that return JsonObject
    Observable<JsonObject> req1=
      client
        .get("/ping/a",RxHttpSupport.uploadJson(new JsonObject().putString("msg","ping/a")))
        .flatMap(RxHttpSupport.downloadJson());
    Observable<JsonObject> req2=
      client
        .get("/ping/b",RxHttpSupport.uploadJson(new JsonObject().putString("msg","ping/b")))
        .flatMap(RxHttpSupport.downloadJson());
    
    // Complete both requests and merge the response
    Observable.zip(req1,req2,
      new Func2<JsonObject,JsonObject,JsonObject>() {
        public JsonObject call(JsonObject resp1, JsonObject resp2) {
          JsonObject res=new JsonObject();
          // Mash resp1 + resp2 into final res object
          res.putObject("resp1",resp1);
          res.putObject("resp2",resp2);
          return res;
        }
      })
      .subscribe(new Action1<JsonObject>() {
        public void call(JsonObject json) {
          System.out.println("zip: "+json);
          tu.azzert("pong".equals(json.getObject("resp1").getString("msg")));
          tu.azzert("/ping/a".equals(json.getObject("resp1").getString("path")));
          tu.azzert("pong".equals(json.getObject("resp2").getString("msg")));
          tu.azzert("/ping/b".equals(json.getObject("resp2").getString("path")));
        }
      },
      RxTestSupport.testFailed(tu),
      RxTestSupport.testComplete(tu));
  }
  
  public void testProxyServer() throws UnsupportedEncodingException {

    // Start the target server
    startPipelineServer(new PingServer(),8080);
    
    // Access via proxy
    client.coreHttpClient().setPort(8081);
    
    client
      // GET JsonObject
      .getNow("/ping")
      // Download body
      .flatMap(RxHttpSupport.downloadJson())
      // Validate
      .subscribe(
        new Action1<JsonObject>() {
          public void call(JsonObject json) {
            System.out.println("zip: "+json);
            tu.azzert("pong".equals(json.getObject("resp1").getString("msg")));
            tu.azzert("/ping/a".equals(json.getObject("resp1").getString("path")));
            tu.azzert("pong".equals(json.getObject("resp2").getString("msg")));
            tu.azzert("/ping/b".equals(json.getObject("resp2").getString("path")));
          }
        },
        RxTestSupport.testFailed(tu),
        RxTestSupport.testComplete(tu));
  }
}
