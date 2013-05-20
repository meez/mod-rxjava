package meez.rxvertx.java;

import meez.rxvertx.java.impl.ResultMemoizeHandler;
import meez.rxvertx.java.impl.SingleObserverHandler;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import rx.Observable;

/** RxJava extension for EventBus */
public class RxEventBus {
  
  /** Nested EventBus */
  private final EventBus core;
  
  /** Create new RxEventBus */
  public RxEventBus(EventBus eventBus) {
    this.core=eventBus;
  }
  
  /** Return core */
  public EventBus coreEventBus() {
    return this.core;
  }
  
  // RxJava extensions
  
  /** Close EventBus */
  public Observable<Void> close() {
    final ResultMemoizeHandler<Void> rh=new ResultMemoizeHandler<Void>();
    core.close(rh);
    return Observable.create(rh.subscribe);
  }
  
  /** Send JsonObject */
  public Observable<Message<JsonObject>> sendRx(String s, JsonObject value) {
    ResultMemoizeHandler<Message<JsonObject>> rh=new ResultMemoizeHandler<Message<JsonObject>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  /** Send JsonArray */
  public Observable<Message<JsonArray>> sendRx(String s, JsonArray value) {
    ResultMemoizeHandler<Message<JsonArray>> rh=new ResultMemoizeHandler<Message<JsonArray>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  /** Send Buffer */
  public Observable<Message<Buffer>> sendRx(String s, Buffer value) {
    ResultMemoizeHandler<Message<Buffer>> rh=new ResultMemoizeHandler<Message<Buffer>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<byte[]>> sendRx(String s, byte[] value) {
    ResultMemoizeHandler<Message<byte[]>> rh=new ResultMemoizeHandler<Message<byte[]>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<String>> sendRx(String s, String value) {
    ResultMemoizeHandler<Message<String>> rh=new ResultMemoizeHandler<Message<String>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<Integer>> sendRx(String s, Integer value) {
    ResultMemoizeHandler<Message<Integer>> rh=new ResultMemoizeHandler<Message<Integer>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<Long>> sendRx(String s, Long value) {
    ResultMemoizeHandler<Message<Long>> rh=new ResultMemoizeHandler<Message<Long>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<Float>> sendRx(String s, Float value) {
    ResultMemoizeHandler<Message<Float>> rh=new ResultMemoizeHandler<Message<Float>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<Double>> sendRx(String s, Double value) {
    ResultMemoizeHandler<Message<Double>> rh=new ResultMemoizeHandler<Message<Double>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<Boolean>> sendRx(String s, Boolean value) {
    ResultMemoizeHandler<Message<Boolean>> rh=new ResultMemoizeHandler<Message<Boolean>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<Short>> sendRx(String s, Short value) {
    ResultMemoizeHandler<Message<Short>> rh=new ResultMemoizeHandler<Message<Short>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<Character>> sendRx(String s, Character value) {
    ResultMemoizeHandler<Message<Character>> rh=new ResultMemoizeHandler<Message<Character>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Message<Byte>> sendRx(String s, Byte value) {
    ResultMemoizeHandler<Message<Byte>> rh=new ResultMemoizeHandler<Message<Byte>>();
    core.send(s,value,rh);
    return Observable.create(rh.subscribe);
  }

  /** Register a handler for a given address
   * 
   * @param address   Address to register
   * 
   * @return Observable<Message<T>> that calls onNext() for each message and onCompleted() when unregistered
   */
  public <T> Observable<Message<T>> toObservable(final String address) {
    SingleObserverHandler<Message<T>,Message<T>> h=new SingleObserverHandler<Message<T>,Message<T>>() {
      @Override
      public void register() {
        core.registerHandler(address,this);
      }
      @Override
      public void clear() {
        core.unregisterHandler(address,this);
      }
    };
    return Observable.create(h.subscribe);
  }

  /** Register a local handler for a given address */
  public <T> Observable<Message<T>> toObservableLocal(final String address) {
    SingleObserverHandler<Message<T>,Message<T>> h=new SingleObserverHandler<Message<T>,Message<T>>() {
      @Override
      public void register() {
        core.registerLocalHandler(address,this);
      }
      @Override
      public void clear() {
        core.unregisterHandler(address,this);
      }
    };
    return Observable.create(h.subscribe);
  }         
  
  // Pipeline methods

  public void unregisterHandler(String s, Handler<? extends Message> handler, AsyncResultHandler<Void> completeHandler) {
    core.unregisterHandler(s,handler,completeHandler);
  }

  public void unregisterHandler(String s, Handler<? extends Message> handler) {
    core.unregisterHandler(s,handler);
  }

  public void registerHandler(String s, Handler<? extends Message> handler, AsyncResultHandler<java.lang.Void> completeHandler) {
    core.registerHandler(s,handler,completeHandler);
  }

  public void registerHandler(String s, Handler<? extends Message> handler) {
    core.registerHandler(s,handler);
  }

  public void registerLocalHandler(String s, Handler<? extends Message> handler) {
    core.registerLocalHandler(s,handler);
  }

  // EventBus methods - providing non-Handler methods for convenience

  public void send(String s, JsonObject jsonObject) {
    core.send(s,jsonObject);
  }

  public void send(String s, JsonArray jsonArray) {
    core.send(s,jsonArray);
  }

  public void send(String s, Buffer buffer) {
    core.send(s,buffer);
  }

  public void send(String s, byte[] bytes) {
    core.send(s,bytes);
  }

  public void send(String s, String s1) {
    core.send(s,s1);
  }

  public void send(String s, Integer integer) {
    core.send(s,integer);
  }

  public void send(String s, Long aLong) {
    core.send(s,aLong);
  }

  public void send(String s, Float aFloat) {
    core.send(s,aFloat);
  }

  public void send(String s, Double aDouble) {
    core.send(s,aDouble);
  }

  public void send(String s, Boolean aBoolean) {
    core.send(s,aBoolean);
  }

  public void send(String s, Short aShort) {
    core.send(s,aShort);
  }

  public void send(String s, Character character) {
    core.send(s,character);
  }

  public void send(String s, Byte aByte) {
    core.send(s,aByte);
  }

  public void publish(String s, JsonObject jsonObject) {
    core.publish(s,jsonObject);
  }

  public void publish(String s, JsonArray jsonArray) {
    core.publish(s,jsonArray);
  }

  public void publish(String s, Buffer buffer) {
    core.publish(s,buffer);
  }

  public void publish(String s, byte[] bytes) {
    core.publish(s,bytes);
  }

  public void publish(String s, String s1) {
    core.publish(s,s1);
  }

  public void publish(String s, Integer integer) {
    core.publish(s,integer);
  }

  public void publish(String s, Long aLong) {
    core.publish(s,aLong);
  }

  public void publish(String s, Float aFloat) {
    core.publish(s,aFloat);
  }

  public void publish(String s, Double aDouble) {
    core.publish(s,aDouble);
  }

  public void publish(String s, Boolean aBoolean) {
    core.publish(s,aBoolean);
  }

  public void publish(String s, Short aShort) {
    core.publish(s,aShort);
  }

  public void publish(String s, Character character) {
    core.publish(s,character);
  }

  public void publish(String s, Byte aByte) {
    core.publish(s,aByte);
  }
}
