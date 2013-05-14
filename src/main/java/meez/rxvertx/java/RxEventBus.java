package meez.rxvertx.java;

import meez.rxvertx.java.subject.*;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import rx.Observable;

/** RxJava extension for EventBus */
public class RxEventBus implements EventBus {
  
  /** Nested EventBus */
  private final EventBus nested;
  
  /** Create new RxEventBus */
  public RxEventBus(EventBus eventBus) {
    this.nested=eventBus;
  }
  
  // RxJava extensions
  
  /** Close EventBus */
  public Observable<Void> closeRx() {
    ReplySubject<Void> rx=ReplySubject.create();
    nested.close(rx);
    return rx;
  }
  
  /** Send JsonObject */
  public Observable<Message<JsonObject>> sendRx(String s, JsonObject value) {
    ReplySubject<Message<JsonObject>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  /** Send JsonArray */
  public Observable<Message<JsonArray>> sendRx(String s, JsonArray value) {
    ReplySubject<Message<JsonArray>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  /** Send Buffer */
  public Observable<Message<Buffer>> sendRx(String s, Buffer value) {
    ReplySubject<Message<Buffer>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<byte[]>> sendRx(String s, byte[] value) {
    ReplySubject<Message<byte[]>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<String>> sendRx(String s, String value) {
    ReplySubject<Message<String>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<Integer>> sendRx(String s, Integer value) {
    ReplySubject<Message<Integer>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<Long>> sendRx(String s, Long value) {
    ReplySubject<Message<Long>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<Float>> sendRx(String s, Float value) {
    ReplySubject<Message<Float>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<Double>> sendRx(String s, Double value) {
    ReplySubject<Message<Double>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<Boolean>> sendRx(String s, Boolean value) {
    ReplySubject<Message<Boolean>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<Short>> sendRx(String s, Short value) {
    ReplySubject<Message<Short>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<Character>> sendRx(String s, Character value) {
    ReplySubject<Message<Character>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  public Observable<Message<Byte>> sendRx(String s, Byte value) {
    ReplySubject<Message<Byte>> rx=ReplySubject.create();
    nested.send(s,value,rx);
    return rx;
  }

  /** Register a handler for a given address
   * 
   * @param s   Address to register
   * 
   * @return Observable<Message<T>> that calls onNext() for each message and onCompleted() when unregistered
   */
  public <T> StreamSubject<Message<T>> registerHandlerRx(String s) {
    StreamSubject<Message<T>> rx=StreamSubject.create();
    nested.registerHandler(s,rx);
    return rx;
  }

  /** Unregister a handler for a given address
   * 
   * @param s         Address to unregister from
   * @param handler   Handler that we need to unregister (result of registerHandlerRx)
   * 
   * @return Observable<Void> that calls onNext()/onCompleted() when unregister completes
   */
  public <T> Observable<Void> unregisterHandlerRx(String s, StreamSubject<Message<T>> handler) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.unregisterHandler(s,handler,rx);
    
    return rx;
  }

  /** Register a local handler for a given address */
  public <T> StreamSubject<Message<T>> registerLocalHandlerRx(String s) {
    StreamSubject<Message<T>> rx=StreamSubject.create();
    nested.registerLocalHandler(s,rx);
    return rx;
  }

  // EventBus implementation

  public void close(Handler<Void> voidHandler) {
    nested.close(voidHandler);
  }

  public void send(String s, JsonObject jsonObject, Handler<Message<JsonObject>> messageHandler) {
    nested.send(s,jsonObject,messageHandler);
  }

  public void send(String s, JsonObject jsonObject) {
    nested.send(s,jsonObject);
  }

  public void send(String s, JsonArray jsonArray, Handler<Message<JsonArray>> messageHandler) {
    nested.send(s,jsonArray,messageHandler);
  }

  public void send(String s, JsonArray jsonArray) {
    nested.send(s,jsonArray);
  }

  public void send(String s, Buffer buffer, Handler<Message<Buffer>> messageHandler) {
    nested.send(s,buffer,messageHandler);
  }

  public void send(String s, Buffer buffer) {
    nested.send(s,buffer);
  }

  public void send(String s, byte[] bytes, Handler<Message<byte[]>> messageHandler) {
    nested.send(s,bytes,messageHandler);
  }

  public void send(String s, byte[] bytes) {
    nested.send(s,bytes);
  }

  public void send(String s, String s1, Handler<Message<String>> messageHandler) {
    nested.send(s,s1,messageHandler);
  }

  public void send(String s, String s1) {
    nested.send(s,s1);
  }

  public void send(String s, Integer integer, Handler<Message<Integer>> messageHandler) {
    nested.send(s,integer,messageHandler);
  }

  public void send(String s, Integer integer) {
    nested.send(s,integer);
  }

  public void send(String s, Long aLong, Handler<Message<Long>> messageHandler) {
    nested.send(s,aLong,messageHandler);
  }

  public void send(String s, Long aLong) {
    nested.send(s,aLong);
  }

  public void send(String s, Float aFloat, Handler<Message<Float>> messageHandler) {
    nested.send(s,aFloat,messageHandler);
  }

  public void send(String s, Float aFloat) {
    nested.send(s,aFloat);
  }

  public void send(String s, Double aDouble, Handler<Message<Double>> messageHandler) {
    nested.send(s,aDouble,messageHandler);
  }

  public void send(String s, Double aDouble) {
    nested.send(s,aDouble);
  }

  public void send(String s, Boolean aBoolean, Handler<Message<Boolean>> messageHandler) {
    nested.send(s,aBoolean,messageHandler);
  }

  public void send(String s, Boolean aBoolean) {
    nested.send(s,aBoolean);
  }

  public void send(String s, Short aShort, Handler<Message<Short>> messageHandler) {
    nested.send(s,aShort,messageHandler);
  }

  public void send(String s, Short aShort) {
    nested.send(s,aShort);
  }

  public void send(String s, Character character, Handler<Message<Character>> messageHandler) {
    nested.send(s,character,messageHandler);
  }

  public void send(String s, Character character) {
    nested.send(s,character);
  }

  public void send(String s, Byte aByte, Handler<Message<Byte>> messageHandler) {
    nested.send(s,aByte,messageHandler);
  }

  public void send(String s, Byte aByte) {
    nested.send(s,aByte);
  }

  public void publish(String s, JsonObject jsonObject) {
    nested.publish(s,jsonObject);
  }

  public void publish(String s, JsonArray jsonArray) {
    nested.publish(s,jsonArray);
  }

  public void publish(String s, Buffer buffer) {
    nested.publish(s,buffer);
  }

  public void publish(String s, byte[] bytes) {
    nested.publish(s,bytes);
  }

  public void publish(String s, String s1) {
    nested.publish(s,s1);
  }

  public void publish(String s, Integer integer) {
    nested.publish(s,integer);
  }

  public void publish(String s, Long aLong) {
    nested.publish(s,aLong);
  }

  public void publish(String s, Float aFloat) {
    nested.publish(s,aFloat);
  }

  public void publish(String s, Double aDouble) {
    nested.publish(s,aDouble);
  }

  public void publish(String s, Boolean aBoolean) {
    nested.publish(s,aBoolean);
  }

  public void publish(String s, Short aShort) {
    nested.publish(s,aShort);
  }

  public void publish(String s, Character character) {
    nested.publish(s,character);
  }

  public void publish(String s, Byte aByte) {
    nested.publish(s,aByte);
  }

  public void unregisterHandler(String s, Handler<? extends Message> handler, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.unregisterHandler(s,handler,voidAsyncResultHandler);
  }

  public void unregisterHandler(String s, Handler<? extends Message> handler) {
    nested.unregisterHandler(s,handler);
  }

  public void registerHandler(String s, Handler<? extends Message> handler, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.registerHandler(s,handler,voidAsyncResultHandler);
  }

  public void registerHandler(String s, Handler<? extends Message> handler) {
    nested.registerHandler(s,handler);
  }

  public void registerLocalHandler(String s, Handler<? extends Message> handler) {
    nested.registerLocalHandler(s,handler);
  }
}
