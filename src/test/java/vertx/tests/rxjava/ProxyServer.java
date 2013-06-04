package vertx.tests.rxjava;

import meez.rxvertx.java.http.RxHttpClient;
import meez.rxvertx.java.http.RxHttpServerRequest;
import meez.rxvertx.java.http.RxHttpSupport;
import meez.rxvertx.java.pipeline.HttpServerPipeline;
import org.vertx.java.core.SimpleHandler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;
import org.vertx.java.testframework.TestUtils;
import rx.Observable;
import rx.util.functions.Func2;

/** Simple ProxyServer that turns a single request into two remote requests and merges the result */
public class ProxyServer extends Verticle {

  protected TestUtils tu;
  private HttpServer server;

  public void start() {
    tu = new TestUtils(vertx);

    // Set pool size to allow concurrent connections
    final RxHttpClient client=new RxHttpClient(vertx.createHttpClient().setHost("localhost").setPort(8080).setMaxPoolSize(16));

    server=vertx.createHttpServer().requestHandler(
      new HttpServerPipeline<JsonObject>() {
        @Override 
        public Observable<JsonObject> process(final RxHttpServerRequest req) {
          
          // Create two async Json requests that return JsonObject
          Observable<JsonObject> req1=client.getNow("/ping/a").flatMap(RxHttpSupport.downloadJson());
          Observable<JsonObject> req2=client.getNow("/ping/b").flatMap(RxHttpSupport.downloadJson());
    
          // Complete both requests and merge the response
          return Observable.zip(req1,req2,
            new Func2<JsonObject,JsonObject,JsonObject>() {
              public JsonObject call(JsonObject resp1, JsonObject resp2) {
                JsonObject res=new JsonObject();
                // Mash resp1 + resp2 into final res object
                res.putObject("resp1",resp1);
                res.putObject("resp2",resp2);
                return res;
              }
            });
        }
      }).listen(8081);

    tu.appReady();
  }

  public void stop() {
    server.close(new SimpleHandler() {
      public void handle() {
        tu.checkContext();
        tu.appStopped();
      }
    });
  }
}
