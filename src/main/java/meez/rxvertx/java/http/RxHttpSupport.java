package meez.rxvertx.java.http;

import java.io.UnsupportedEncodingException;

import meez.rxvertx.java.subject.ReplySubject;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.*;
import org.vertx.java.core.json.JsonObject;
import rx.Observable;
import rx.util.functions.Action1;
import rx.util.functions.Func1;

/** Utility methods for RxHttpXXX */
public class RxHttpSupport {

  // Server
  
  public static Func1<HttpServerRequest,Observable<Buffer>> decodeBody=new Func1<HttpServerRequest,Observable<Buffer>>() {
    public Observable<Buffer> call(HttpServerRequest httpReq) {
      ReplySubject<Buffer> rx=ReplySubject.create();
      httpReq.bodyHandler(rx);
      return rx;
    }
  };
  
  // Client
  
  // Uploaders

  /** Create uploader for JsonObject */
  public static Action1<HttpClientRequest> uploadJson(JsonObject src, String charset) throws UnsupportedEncodingException {
    String contentType="text/json;charset="+charset;
    return uploadBody(contentType,src.encode().getBytes(charset));    
  }
  
  /** Create uploader for byte array */
  public static Action1<HttpClientRequest> uploadBody(final String contentType, final byte[] src) {
    return new Action1<HttpClientRequest>() {
      public void call(HttpClientRequest httpReq) {
        httpReq.putHeader("Content-type",contentType);
        httpReq.putHeader("Content-length",src.length);
        httpReq.write(new Buffer(src));
        httpReq.end();
      }
    };
  }
  
  // Downloads
  
  /** Create decoder for raw response */
  public static Func1<HttpClientResponse, Observable<Buffer>> downloadBody() {
    return new Func1<HttpClientResponse, Observable<Buffer>>() {
      public Observable<Buffer> call(HttpClientResponse httpResp) {
        ReplySubject<Buffer> rx=ReplySubject.create();
        httpResp.bodyHandler(rx);
        return rx;
      }
    };
  }
}
