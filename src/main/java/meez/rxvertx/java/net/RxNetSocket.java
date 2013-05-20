package meez.rxvertx.java.net;

import meez.rxvertx.java.RxSupport;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.core.streams.ReadStream;
import org.vertx.java.core.streams.WriteStream;
import rx.Observable;

/** RxNetSocket */
public class RxNetSocket implements ReadStream, WriteStream {
  
  private NetSocket nested;
  
  public RxNetSocket(NetSocket nested) {
    this.nested=nested;
  }
  
  /** Return as Observable<Buffer> */
  public Observable<Buffer> asObservable() {
    return RxSupport.toObservable(nested);
  }
  
  public void close() {
    nested.close();
  }
  
  // NetSocket implementation
  
  public RxNetSocket write(Buffer b) {
    nested.write(b);
    return this;
  }
  
  public RxNetSocket write(String s) {
    nested.write(s);
    return this;
  }
  
  public RxNetSocket write(String s, String charset) {
    nested.write(s,charset);
    return this;
  }
  
  public RxNetSocket write(Buffer b, Handler<Void> completeHandler) {
    nested.write(b,completeHandler);
    return this;
  }
  
  public RxNetSocket write(String s, Handler<java.lang.Void> completeHandler) {
    nested.write(s,completeHandler);
    return this;
  }
  
  public RxNetSocket write(String s, String charset, Handler<Void> completeHandler) {
    nested.write(s,charset,completeHandler);
    return this;
  }
  
  public void sendFile(java.lang.String s) {
    nested.sendFile(s);
  }
  
  // ReadStream implementation
  
  public void dataHandler(Handler<Buffer> bufferHandler) {
    throw new UnsupportedOperationException("Not supported in Rx - use asObservable");
  }
  
  public void pause() {
    nested.pause();
  }
  
  public void resume() {
    nested.resume();
  }
  
  public void exceptionHandler(Handler<java.lang.Exception> exceptionHandler) {
    throw new UnsupportedOperationException("Not supported in Rx - use asObservable");
  }
  
  public void endHandler(Handler<java.lang.Void> voidHandler) {
    throw new UnsupportedOperationException("Not supported in Rx - use asObservable");
  }
  
  // WriteStream implementation

  public void writeBuffer(Buffer buffer) {
    nested.write(buffer);
  }

  public void setWriteQueueMaxSize(int i) {
    nested.setWriteQueueMaxSize(i);
  }

  public boolean writeQueueFull() {
    return nested.writeQueueFull();
  }

  public void drainHandler(Handler<Void> voidHandler) {
    nested.drainHandler(voidHandler);
  }
}
