package vertx.tests.rxjava;

import java.util.*;

import meez.rxvertx.java.*;
import meez.rxvertx.java.pipeline.EventBusPipeline;
import meez.rxvertx.java.subject.StreamSubject;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.testframework.TestClientBase;
import rx.Observable;
import rx.util.functions.*;

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
          tu.azzert(exp.isEmpty());
        }
      });
  }
  
  /** Unregister fn */
  protected <T> Action0 fnUnregister(final RxEventBus eb, final String address, final StreamSubject<Message<T>> in) {
    return new Action0() {
      public void call() {
        eb.unregisterHandler(address,in);
        in.onCompleted();
        tu.testComplete();
      }
    };
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
    
    final StreamSubject<Message<String>> server=eb.registerHandlerRx("foo");
    
    assertOne(server,"bar",fnUnregister(eb,"foo",server));
    
    eb.send("foo","bar");
  }

  /** Data-type loopback test */
  public void testSendRx() throws Exception {
    
    final RxEventBus eb=rxv.eventBus();
    
    final StreamSubject<Message<Object>> server=eb.registerHandlerRx("foo");
    
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
        typeJsonObject,
        typeJsonArray,
        typeBuffer,
        typeString,
        typeInteger,
        typeLong,
        typeFloat,
        typeDouble,
        typeBoolean,
        typeShort,
        typeCharacter,
        typeByteArray,
        typeByte),
      fnUnregister(eb,"foo",server));
    
    eb.sendRx("foo",typeJsonObject).subscribe(checkLoopBack(typeJsonObject));
    eb.sendRx("foo",typeJsonArray).subscribe(checkLoopBack(typeJsonArray));
    eb.sendRx("foo",typeBuffer).subscribe(checkLoopBack(typeBuffer));
    eb.sendRx("foo",typeString).subscribe(checkLoopBack(typeString));
    eb.sendRx("foo",typeInteger).subscribe(checkLoopBack(typeInteger));
    eb.sendRx("foo",typeLong).subscribe(checkLoopBack(typeLong));
    eb.sendRx("foo",typeFloat).subscribe(checkLoopBack(typeFloat));
    eb.sendRx("foo",typeDouble).subscribe(checkLoopBack(typeDouble));
    eb.sendRx("foo",typeBoolean).subscribe(checkLoopBack(typeBoolean));
    eb.sendRx("foo",typeShort).subscribe(checkLoopBack(typeShort));
    eb.sendRx("foo",typeCharacter).subscribe(checkLoopBack(typeCharacter));
    eb.sendRx("foo",typeByteArray).subscribe(checkLoopBack(typeByteArray));
    eb.sendRx("foo",typeByte).subscribe(checkLoopBack(typeByte));
    eb.closeRx().subscribe(new Action1<Void>() {
      public void call(Void aVoid) {
        System.out.println("RxEventBus closed");
        tu.testComplete();
      }
    });
  }

  public void testRegisterHandlerRx() throws Exception {

    final RxEventBus eb=rxv.eventBus();
    
    final StreamSubject<Message<String>> server=eb.registerHandlerRx("foo");

    assertSequence(server,toList("all","bar","none"),fnUnregister(eb,"foo",server));
    
    eb.send("foo","all");
    eb.send("foo","bar");
    eb.send("foo","none");
  }
  
  // JSON
  
  public static class Pojo {
    public boolean bool;
    public int num;
    public String str;

    public Pojo() {
    }
    
    public Pojo(boolean bool, int num, String str) {
      this.bool=bool;
      this.num=num;
      this.str=str;
    }
    
    public String toString() {
      return "Pojo("+bool+","+num+","+str+")";
    }
    
    public boolean equals(Object o) {
      if ((o==null) || ! (o instanceof Pojo))
        return false;
      Pojo op=(Pojo)o;
      return this.bool==op.bool && this.num==op.num && this.str.equals(op.str);
    }
  }
  
  public void testRxObjectMapping() throws Exception {
    
    final RxEventBus eb=rxv.eventBus();
    
    final StreamSubject<Message<JsonObject>> server=eb.registerHandlerRx("json");

    server
      // Decode message to a Buffer
      .map(RxSupport.unwrapMessage)
      // then to a POJO
      .map(RxSupport.jsonToObject(Pojo.class))
      // Confirm
      .subscribe(new Action1<Pojo>() {
          public void call(Pojo in) {
            tu.azzert(false==in.bool);
            tu.azzert(-29492==in.num);
            tu.azzert("bar".equals(in.str));
            // Sepuku
            eb.unregisterHandlerRx("json",server);
            // Done
            tu.testComplete();
          }
        });
    
    eb.send("json",new Buffer("{\"bool\":false,\"num\":-29492,\"str\":\"bar\"}".getBytes()));
  }
  
  // Pipelines
  
  public void testRxEventBusPipeline() throws Exception {
    
    final RxEventBus eb=rxv.eventBus();

    // Register ping server
    eb.registerHandler("/test/ping",new EventBusPipeline<JsonObject>() {
      public Observable<JsonObject> processRequest(Observable<JsonObject> req) {
        // Bounce ping->pong
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
      .subscribe(new Action1<Message<JsonObject>>() {
        public void call(Message<JsonObject> in) {
          tu.azzert("pong".equals(in.body.getString("msg")));
          tu.testComplete();
        }
      });

    System.out.println("ping listener registered");            
  }
}
