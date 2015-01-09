package vertx.tests.rxjava;

import meez.rxvertx.java.RxSupport;
import meez.rxvertx.java.http.RxHttpServerRequest;
import meez.rxvertx.java.http.RxHttpSupport;
import meez.rxvertx.java.pipeline.HttpServerPipeline;
import org.vertx.java.core.SimpleHandler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;
import org.vertx.java.testframework.TestUtils;
import rx.Observable;
import rx.functions.Func1;

/** JsonServer */
public class JsonServer extends Verticle {

  protected TestUtils tu;
  private HttpServer server;

  public void start() {
    tu = new TestUtils(vertx);
    server=vertx.createHttpServer().requestHandler(
      new HttpServerPipeline<JsonObject>() {
        @Override 
        public Observable<JsonObject> process(final RxHttpServerRequest req) {
          tu.checkContext();
          return Observable.just(req)
            // Parse the request body as a JsonObject
            .flatMap(RxHttpSupport.decodeBody)
            .map(RxSupport.decodeJson("utf8"))
            // Simple pong responder
            .map(new Func1<JsonObject,JsonObject>() {
              public JsonObject call(JsonObject in) {
                return new JsonObject()
                  .putString("msg","pong")
                  .putString("path",req.path)
                  .putString("src",in.getString("msg"));
              }
            });
        }
      }).listen(8080);

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
