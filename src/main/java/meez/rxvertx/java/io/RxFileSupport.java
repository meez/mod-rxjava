package meez.rxvertx.java.io;

import meez.rxvertx.java.RxSupport;
import meez.rxvertx.java.impl.AsyncResultMemoizeHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.file.AsyncFile;
import rx.Observable;
import rx.functions.Func1;

/** RxFileSupport utility methods */
public class RxFileSupport {
  
  /** Close a file */
  public static Observable<Void> closeRx(AsyncFile f) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    f.close(rh);
    return Observable.create(rh.subscribe);
  }
  
  /** Read a file stream */
  public static Func1<AsyncFile, Observable<Buffer>> readStream=new Func1<AsyncFile,Observable<Buffer>>() {
    public Observable<Buffer> call(AsyncFile asyncFile) {
      return RxSupport.toObservable(asyncFile.getReadStream()); 
    }
  };
}
