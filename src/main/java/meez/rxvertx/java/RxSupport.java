package meez.rxvertx.java;

import meez.rxvertx.java.subject.StreamSubject;
import org.codehaus.jackson.map.ObjectMapper;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.streams.ReadStream;
import org.vertx.java.core.streams.WriteStream;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.util.functions.Action0;
import rx.util.functions.Action1;
import rx.util.functions.Func1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicLong;

/** RxSupport */
public class RxSupport {
  
  // Streams

  /** Stream Observable<Buffer> to WriteStream.
   *
   * <p>This method does not handle writeQueueFull condition</p>  
   * 
   **/
  public static Observable<Long> stream(Observable<Buffer> src, final WriteStream out) {
    final PublishSubject<Long> rx=PublishSubject.create();
    final AtomicLong total=new AtomicLong();
    src.subscribe(
      new Action1<Buffer>() {
        public void call(Buffer buffer) {
          out.writeBuffer(buffer);
          total.addAndGet(buffer.length());
        }
      },
      new Action1<Exception>() {
        public void call(Exception e) {
          rx.onError(e);
        }
      },
      new Action0() {
        public void call() {
          rx.onNext(total.get());
          rx.onCompleted();
        }
      }
    );
    return rx;
  }

  /** Convert ReadStream to Observable */
  public static Observable<Buffer> toObservable(ReadStream rs) {
    final StreamSubject<Buffer> rx=StreamSubject.create();
    // Stream buffers to the Observable
    rs.dataHandler(rx);
    // Map exception
    rs.exceptionHandler(new Handler<Exception>() {
      public void handle(Exception e) {
        rx.onError(e);
      }
    });
    // Map complete
    rs.endHandler(new Handler<Void>() {
      public void handle(Void v) {
        rx.onCompleted();
      }
    });
    return rx;
  }
  
  /** Inline Func1 */
  public static Func1<ReadStream, Observable<Buffer>> readStream=new Func1<ReadStream,Observable<Buffer>>() {
    public Observable<Buffer> call(ReadStream readStream) {
      return toObservable(readStream);
    }
  };
  
  // JSON 
	
  /** Simple JSON encode */
  public static Func1<JsonObject, Buffer> encodeJson(final String charset) {
    return new Func1<JsonObject,Buffer>() {
      public Buffer call(JsonObject in) {
        try {
          return new Buffer(in.encode().getBytes(charset));
        }
        catch (UnsupportedEncodingException e) {
          throw new RxException("Unable to encode JSON (charset="+charset+")",e);
        }
      }
    };
  }
  
  /** Simple JSON decode */
  public static Func1<Buffer,JsonObject> decodeJson(final String charset) {
    return new Func1<Buffer,JsonObject>() {
      public JsonObject call(Buffer in) {
        try {
          return new JsonObject(in.toString(charset));
        }
        catch(Exception e) {
          throw new RxException("Unable to decode json request (e="+e+")");
        }
      }
    };
  }

  /** Object mapper */
  public static <T> Func1<T,Buffer> objectToJson(final Class<T> def)
  {
    return new Func1<T,Buffer>() {
      private final ObjectMapper om=new ObjectMapper();
      public Buffer call(T in) {
        try {
          byte[] encoded=om.writeValueAsBytes(in);
          return new Buffer(encoded);
        }
        catch (IOException e) {
          throw new RxException("Unable to decode json object",e);
        }
      }
    };
  }

  /** Object mapper */
  public static <T> Func1<Buffer,T> jsonToObject(final Class<T> def)
  {
    return new Func1<Buffer,T>() {
      private final ObjectMapper om=new ObjectMapper();
      public T call(Buffer in) {
        try {
          return om.readValue(in.getBytes(),def);
        }
        catch (IOException e) {
          throw new RxException("Unable to decode json object",e);
        }
      }
    };
  }
	
  // EventBus
  
  /** Message */
  public static Func1<Message,Object> unwrapMessage=new Func1<Message,Object>() {
    public Object call(Message msg) {
      return msg.body;
    }
  };
  
  /** Validates the standard BusModBase JSON reply for status and triggers exception if failed */
  public static Func1<JsonObject, JsonObject> handleBusModReply=new Func1<JsonObject,JsonObject>() {
    public JsonObject call(JsonObject in) {
      if (!"ok".equalsIgnoreCase(in.getString("status","fail"))) {
        // TODO: Extract error and log / include in exception
        throw new RxException("Request failed");
      }
      return in;
    }
  };
}
