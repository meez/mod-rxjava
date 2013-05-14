package meez.rxvertx.java.io;

import meez.rxvertx.java.RxSupport;
import meez.rxvertx.java.subject.AsyncResultSubject;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.file.AsyncFile;
import rx.Observable;
import rx.util.functions.Func1;

/** RxFileSupport utility methods */
public class RxFileSupport {
  
  /** Close a file */
  public static Observable<Void> closeRx(AsyncFile f) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    f.close(rx);
    return rx;
  }
  
  /** Read a file stream */
  public static Func1<AsyncFile, Observable<Buffer>> readStream=new Func1<AsyncFile,Observable<Buffer>>() {
    public Observable<Buffer> call(AsyncFile asyncFile) {
      return RxSupport.toObservable(asyncFile.getReadStream()); 
    }
  };
}
