package vertx.tests.rxjava;

import meez.rxvertx.java.RxEventBus;
import meez.rxvertx.java.RxTestSupport;
import meez.rxvertx.java.RxVertx;
import meez.rxvertx.java.pipeline.EventBusPipeline;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.testframework.TestClientBase;
import rx.Observable;
import rx.util.functions.Action0;
import rx.util.functions.Action1;
import rx.util.functions.Func1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** EventBus */
public class EventBus extends TestClientBase {

  // Helpers
  
  /** Convert elements into a mutable List */
  protected List toList(Object... a) {
    List<Object> res=new ArrayList<Object>(a.length);
    for (int i=0;i<a.length;i++) {
      res.add(a[i]);
    }
    return res;
  }
  
  /** Assert that the observable returns a single object */
  protected void assertOne(Observable<? extends Object> in, Object exp, Action0 onRead) {
    List<Object> c=new LinkedList<Object>();
    c.add(exp);
    assertSequence(in, c, onRead);
  }

  /** Assert that the observable returns the given sequence */
  protected void assertSequence(Observable<? extends Object> in, final List exp, final Action0 onReadComplete) {
    in.subscribe(
      // onNext
      new Action1<Object>() {
        public void call(Object v) {
          
          tu.azzert(!exp.isEmpty());
          tu.azzert(v!=null);
          
          // Unwrap message by default
          if (v instanceof Message)
            v=((Message)v).body;
          
          System.out.println("expected="+exp.get(0)+" actual="+v);
          
          azzertEquals(exp.remove(0),v);
          
          // Call onRead once the expected data has all been read
          if (exp.isEmpty()) {
            onReadComplete.call();
          }
        }
      },
      // onError
      new Action1<Exception>() {
        public void call(Exception e) {
          System.err.println("Pipeline failed "+e);
          e.printStackTrace(System.err);
          tu.azzert(false);
        }
      },
      // onCompleted
      new Action0() {
        public void call() {
          System.out.println("Server complete");
          tu.azzert(exp.isEmpty());
        }
      });
  }
  
  protected void azzertEquals(Object a, Object b) {
    if ((a==null) || (b==null)) {
      tu.azzert(false);
      return;
    }
    // HACK: Verify byte[] on a byte-by-byte basis
    if (a instanceof byte[]) {
      byte[] ba=(byte[])a;
      byte[] bb=(byte[])b;
      tu.azzert(ba.length==bb.length);
      for (int i=0;i<ba.length;i++)
        tu.azzert(ba[i]==bb[i]);
    }
    else {
      tu.azzert(a.equals(b));
    }
  }
  
  protected <T> Action1<Message<JsonObject>> checkLoopBack(final T expected) {
    return new Action1<Message<JsonObject>>() {
      public void call(Message<JsonObject> in) {
        System.out.println("loopback("+expected+")="+in.body);
        azzertEquals(expected,in.body);
      }
    };
  }
  
  protected Action0 completeTest() {
    return new Action0 () {
      public void call() {
        System.out.println("test complete");
        tu.testComplete();
      }
    };
  }

  protected Action0 checkpointTest() {
    return new Action0 () {
      public void call() {
        System.out.println("test checkpoint");
      }
    };
  }

  // Setup

  private RxVertx rxv;
  
  @Override
  public void start() {
    super.start();

    this.rxv=new RxVertx(vertx);

    tu.appReady();
  }
  
  @Override 
  public void stop() {
    super.stop();
  }
  
  // Tests
  
  /** Basic string test */
  public void testSend() throws Exception {
    
    final RxEventBus eb=rxv.eventBus();
    
    final Observable<Message<String>> server=eb.toObservable("send");
    
    assertOne(server,"bar", completeTest());
    
    eb.send("send","bar");
  }

  /** Data-type loopback test */
  public void testSendRx() throws Exception {
    
    final RxEventBus eb=rxv.eventBus();
    
    final Observable<Message<Object>> server=eb.toObservable("sendrx");
    
    JsonObject typeJsonObject=new JsonObject().putString("a","str").putNumber("b",-100);
    JsonArray typeJsonArray=new JsonArray().addObject(typeJsonObject);
    Buffer typeBuffer=new Buffer("example");
    String typeString="example";
    Integer typeInteger=Integer.MAX_VALUE;
    Long typeLong=Long.MIN_VALUE;
    Float typeFloat=Float.MAX_VALUE;
    Double typeDouble=Double.MIN_VALUE;
    Boolean typeBoolean=false;
    Short typeShort=Short.MAX_VALUE;
    Character typeCharacter='a';
    byte[] typeByteArray="example".getBytes();
    Byte typeByte=0x13;
    
    assertSequence(
      server,
      toList(
        typeString,
        typeJsonObject,
        typeJsonArray,
        typeBuffer,
        typeInteger,
        typeLong,
        typeFloat,
        typeDouble,
        typeBoolean,
        typeShort,
        typeCharacter,
        typeByteArray,
        typeByte),
      completeTest());
    
    eb.sendRx("sendrx",typeString).subscribe(checkLoopBack(typeJsonObject));
    eb.sendRx("sendrx",typeJsonObject).subscribe(checkLoopBack(typeJsonObject));
    eb.sendRx("sendrx",typeJsonArray).subscribe(checkLoopBack(typeJsonArray));
    eb.sendRx("sendrx",typeBuffer).subscribe(checkLoopBack(typeBuffer));
    eb.sendRx("sendrx",typeInteger).subscribe(checkLoopBack(typeInteger));
    eb.sendRx("sendrx",typeLong).subscribe(checkLoopBack(typeLong));
    eb.sendRx("sendrx",typeFloat).subscribe(checkLoopBack(typeFloat));
    eb.sendRx("sendrx",typeDouble).subscribe(checkLoopBack(typeDouble));
    eb.sendRx("sendrx",typeBoolean).subscribe(checkLoopBack(typeBoolean));
    eb.sendRx("sendrx",typeShort).subscribe(checkLoopBack(typeShort));
    eb.sendRx("sendrx",typeCharacter).subscribe(checkLoopBack(typeCharacter));
    eb.sendRx("sendrx",typeByteArray).subscribe(checkLoopBack(typeByteArray));
    eb.sendRx("sendrx",typeByte).subscribe(checkLoopBack(typeByte));
  }

  public void testToObservableLocal() throws Exception {

    final RxEventBus eb=rxv.eventBus();
    
    final Observable<Message<String>> server=eb.toObservableLocal("ob-local");

    assertSequence(server,toList("all","bar","none"),completeTest());
    
    eb.send("ob-local","all");
    eb.send("ob-local","bar");
    eb.send("ob-local","none");
  }
  
  // Pipelines
  
  public void testEventBusPipeline() throws Exception {
    
    final RxEventBus eb=rxv.eventBus();

    // Register ping server
    eb.registerHandler("/test/ping",new EventBusPipeline<JsonObject>() {
      public Observable<JsonObject> processRequest(Observable<JsonObject> req) {
        // Bounce ping->pong
        System.out.println("pipeline:ping");
        return req.map(new Func1<JsonObject,JsonObject>() {
          public JsonObject call(JsonObject in) {
            tu.azzert("ping".equals(in.getString("msg")));
            return new JsonObject().putString("msg","pong");
          }
        });
      }
    });

    // Send ping and wait for pong
    eb.sendRx("/test/ping",new JsonObject().putString("msg","ping"))
      .map(RxTestSupport.traceMap("pipeline:pong"))
      .subscribe(new Action1<Message<JsonObject>>() {
        public void call(Message<JsonObject> in) {
          tu.azzert("pong".equals(in.body.getString("msg")));
          tu.testComplete();
        }
      });
  }
}
