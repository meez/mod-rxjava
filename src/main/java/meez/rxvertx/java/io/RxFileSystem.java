package meez.rxvertx.java.io;

import meez.rxvertx.java.subject.AsyncResultSubject;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.file.*;
import rx.Observable;

/** RxJava extension to FileSystem */
public class RxFileSystem implements FileSystem {
  
  /** Nested FileSystem */
  protected final FileSystem nested;

  /** Create new RxFileSystem */
  public RxFileSystem(FileSystem fileSystem) {
    this.nested=fileSystem;
  }
  
  // Rx extensions
  
  public Observable<Void> copyRx(String s, String s1) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.copy(s,s1,rx);
    return rx;
  }

  public Observable<Void> copyRx(String s, String s1, boolean b) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.copy(s,s1,b,rx);
    return rx;
  }

  public Observable<Void> moveRx(String s, String s1) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.move(s,s1,rx);
    return rx;
  }

  public Observable<Void> truncateRx(String s, long l) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.truncate(s,l,rx);
    return rx;
  }

  public Observable<Void> chmodRx(String s, String s1) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.chmod(s,s1,rx);
    return rx;
  }

  public Observable<Void> chmodRx(String s, String s1, String s2) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.chmod(s,s1,s2,rx);
    return rx;
  }

  public Observable<FileProps> propsRx(String s) {
    AsyncResultSubject<FileProps> rx=AsyncResultSubject.create();
    nested.props(s,rx);
    return rx;
  }

  public Observable<FileProps> lpropsRx(String s) {
    AsyncResultSubject<FileProps> rx=AsyncResultSubject.create();
    nested.lprops(s,rx);
    return rx;
  }

  public Observable<Void> linkRx(String s, String s1) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.link(s,s1,rx);
    return rx;
  }

  public Observable<Void> symlinkRx(String s, String s1) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.symlink(s,s1,rx);
    return rx;
  }

  public Observable<Void> unlinkRx(String s) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.unlink(s,rx);
    return rx;
  }

  public Observable<String> readSymlinkRx(String s) {
    AsyncResultSubject<String> rx=AsyncResultSubject.create();
    nested.readSymlink(s,rx);
    return rx;
  }

  public Observable<Void> deleteRx(String s) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.delete(s,rx);
    return rx;
  }

  public Observable<Void> deleteRx(String s, boolean b) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.delete(s,b,rx);
    return rx;
  }

  public Observable<Void> mkdirRx(String s) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.mkdir(s,rx);
    return rx;
  }

  public Observable<Void> mkdirRx(String s, boolean b) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.mkdir(s,b,rx);
    return rx;
  }

  public Observable<Void> mkdirRx(String s, String s1) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.mkdir(s,s1,rx);
    return rx;
  }

  public Observable<Void> mkdirRx(String s, String s1, boolean b) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.mkdir(s,s1,b,rx);
    return rx;
  }

  public Observable<String[]> readDirRx(String s) {
    AsyncResultSubject<String[]> rx=AsyncResultSubject.create();
    nested.readDir(s,rx);
    return rx;
  }

  public Observable<String[]> readDirRx(String s, String s1) {
    AsyncResultSubject<String[]> rx=AsyncResultSubject.create();
    nested.readDir(s,s1,rx);
    return rx;
  }

  public Observable<Buffer> readFileRx(String s) {
    AsyncResultSubject<Buffer> rx=AsyncResultSubject.create();
    nested.readFile(s,rx);
    return rx;
  }

  public Observable<Void> writeFileRx(String s, Buffer buffer) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.writeFile(s,buffer,rx);
    return rx;
  }

  public Observable<AsyncFile> openRx(String s) {
    AsyncResultSubject<AsyncFile> rx=AsyncResultSubject.create();
    nested.open(s,rx);
    return rx;
  }

  public Observable<AsyncFile> openRx(String s, String s1) {
    AsyncResultSubject<AsyncFile> rx=AsyncResultSubject.create();
    nested.open(s,s1,rx);
    return rx;
  }

  public Observable<AsyncFile> openRx(String s, String s1, boolean b) {
    AsyncResultSubject<AsyncFile> rx=AsyncResultSubject.create();
    nested.open(s,s1,b,rx);
    return rx;
  }

  public Observable<AsyncFile> openRx(String s, String s1, boolean b, boolean b1, boolean b2) {
    AsyncResultSubject<AsyncFile> rx=AsyncResultSubject.create();
    nested.open(s,s1,b,b1,b2,rx);
    return rx;
  }

  public Observable<AsyncFile> openRx(String s, String s1, boolean b, boolean b1, boolean b2, boolean b3) {
    AsyncResultSubject<AsyncFile> rx=AsyncResultSubject.create();
    nested.open(s,s1,b,b1,b2,b3,rx);
    return rx;
  }

  public Observable<Void> createFileRx(String s) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.createFile(s,rx);
    return rx;
  }

  public Observable<Void> createFileRx(String s, String s1) {
    AsyncResultSubject<Void> rx=AsyncResultSubject.create();
    nested.createFile(s,s1,rx);
    return rx;
  }

  public Observable<Boolean> existsRx(String s) {
    AsyncResultSubject<Boolean> rx=AsyncResultSubject.create();
    nested.exists(s,rx);
    return rx;
  }

  public Observable<FileSystemProps> fsPropsRx(String s) {
    AsyncResultSubject<FileSystemProps> rx=AsyncResultSubject.create();
    nested.fsProps(s,rx);
    return rx;
  }

  // FileSystem implementation

  public void copy(String s, String s1, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.copy(s,s1,voidAsyncResultHandler);
  }

  public void copySync(String s, String s1) throws Exception {
    nested.copySync(s,s1);
  }

  public void copy(String s, String s1, boolean b, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.copy(s,s1,b,voidAsyncResultHandler);
  }

  public void copySync(String s, String s1, boolean b) throws Exception {
    nested.copySync(s,s1,b);
  }

  public void move(String s, String s1, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.move(s,s1,voidAsyncResultHandler);
  }

  public void moveSync(String s, String s1) throws Exception {
    nested.moveSync(s,s1);
  }

  public void truncate(String s, long l, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.truncate(s,l,voidAsyncResultHandler);
  }

  public void truncateSync(String s, long l) throws Exception {
    nested.truncateSync(s,l);
  }

  public void chmod(String s, String s1, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.chmod(s,s1,voidAsyncResultHandler);
  }

  public void chmodSync(String s, String s1) throws Exception {
    nested.chmodSync(s,s1);
  }

  public void chmod(String s, String s1, String s2, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.chmod(s,s1,s2,voidAsyncResultHandler);
  }

  public void chmodSync(String s, String s1, String s2) throws Exception {
    nested.chmodSync(s,s1,s2);
  }

  public void props(String s, AsyncResultHandler<FileProps> filePropsAsyncResultHandler) {
    nested.props(s,filePropsAsyncResultHandler);
  }

  public FileProps propsSync(String s) throws Exception {
    return nested.propsSync(s);
  }

  public void lprops(String s, AsyncResultHandler<FileProps> filePropsAsyncResultHandler) {
    nested.lprops(s,filePropsAsyncResultHandler);
  }

  public FileProps lpropsSync(String s) throws Exception {
    return nested.lpropsSync(s);
  }

  public void link(String s, String s1, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.link(s,s1,voidAsyncResultHandler);
  }

  public void linkSync(String s, String s1) throws Exception {
    nested.linkSync(s,s1);
  }

  public void symlink(String s, String s1, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.symlink(s,s1,voidAsyncResultHandler);
  }

  public void symlinkSync(String s, String s1) throws Exception {
    nested.symlinkSync(s,s1);
  }

  public void unlink(String s, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.unlink(s,voidAsyncResultHandler);
  }

  public void unlinkSync(String s) throws Exception {
    nested.unlinkSync(s);
  }

  public void readSymlink(String s, AsyncResultHandler<String> stringAsyncResultHandler) {
    nested.readSymlink(s,stringAsyncResultHandler);
  }

  public String readSymlinkSync(String s) throws Exception {
    return nested.readSymlinkSync(s);
  }

  public void delete(String s, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.delete(s,voidAsyncResultHandler);
  }

  public void deleteSync(String s) throws Exception {
    nested.deleteSync(s);
  }

  public void delete(String s, boolean b, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.delete(s,b,voidAsyncResultHandler);
  }

  public void deleteSync(String s, boolean b) throws Exception {
    nested.deleteSync(s,b);
  }

  public void mkdir(String s, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.mkdir(s,voidAsyncResultHandler);
  }

  public void mkdirSync(String s) throws Exception {
    nested.mkdirSync(s);
  }

  public void mkdir(String s, boolean b, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.mkdir(s,b,voidAsyncResultHandler);
  }

  public void mkdirSync(String s, boolean b) throws Exception {
    nested.mkdirSync(s,b);
  }

  public void mkdir(String s, String s1, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.mkdir(s,s1,voidAsyncResultHandler);
  }

  public void mkdirSync(String s, String s1) throws Exception {
    nested.mkdirSync(s,s1);
  }

  public void mkdir(String s, String s1, boolean b, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.mkdir(s,s1,b,voidAsyncResultHandler);
  }

  public void mkdirSync(String s, String s1, boolean b) throws Exception {
    nested.mkdirSync(s,s1,b);
  }

  public void readDir(String s, AsyncResultHandler<String[]> asyncResultHandler) {
    nested.readDir(s,asyncResultHandler);
  }

  public String[] readDirSync(String s) throws Exception {
    return nested.readDirSync(s);
  }

  public void readDir(String s, String s1, AsyncResultHandler<String[]> asyncResultHandler) {
    nested.readDir(s,s1,asyncResultHandler);
  }

  public String[] readDirSync(String s, String s1) throws Exception {
    return nested.readDirSync(s,s1);
  }

  public void readFile(String s, AsyncResultHandler<Buffer> bufferAsyncResultHandler) {
    nested.readFile(s,bufferAsyncResultHandler);
  }

  public Buffer readFileSync(String s) throws Exception {
    return nested.readFileSync(s);
  }

  public void writeFile(String s, Buffer buffer, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.writeFile(s,buffer,voidAsyncResultHandler);
  }

  public void writeFileSync(String s, Buffer buffer) throws Exception {
    nested.writeFileSync(s,buffer);
  }

  public void open(String s, AsyncResultHandler<AsyncFile> asyncFileAsyncResultHandler) {
    nested.open(s,asyncFileAsyncResultHandler);
  }

  public AsyncFile openSync(String s) throws Exception {
    return nested.openSync(s);
  }

  public void open(String s, String s1, AsyncResultHandler<AsyncFile> asyncFileAsyncResultHandler) {
    nested.open(s,s1,asyncFileAsyncResultHandler);
  }

  public AsyncFile openSync(String s, String s1) throws Exception {
    return nested.openSync(s,s1);
  }

  public void open(String s, String s1, boolean b, AsyncResultHandler<AsyncFile> asyncFileAsyncResultHandler) {
    nested.open(s,s1,b,asyncFileAsyncResultHandler);
  }

  public AsyncFile openSync(String s, String s1, boolean b) throws Exception {
    return nested.openSync(s,s1,b);
  }

  public void open(String s, String s1, boolean b, boolean b1, boolean b2, AsyncResultHandler<AsyncFile> asyncFileAsyncResultHandler) {
    nested.open(s,s1,b,b1,b2,asyncFileAsyncResultHandler);
  }

  public AsyncFile openSync(String s, String s1, boolean b, boolean b1, boolean b2) throws Exception {
    return nested.openSync(s,s1,b,b1,b2);
  }

  public void open(String s, String s1, boolean b, boolean b1, boolean b2, boolean b3, AsyncResultHandler<AsyncFile> asyncFileAsyncResultHandler) {
    nested.open(s,s1,b,b1,b2,b3,asyncFileAsyncResultHandler);
  }

  public AsyncFile openSync(String s, String s1, boolean b, boolean b1, boolean b2, boolean b3) throws Exception {
    return nested.openSync(s,s1,b,b1,b2,b3);
  }

  public void createFile(String s, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.createFile(s,voidAsyncResultHandler);
  }

  public void createFileSync(String s) throws Exception {
    nested.createFileSync(s);
  }

  public void createFile(String s, String s1, AsyncResultHandler<Void> voidAsyncResultHandler) {
    nested.createFile(s,s1,voidAsyncResultHandler);
  }

  public void createFileSync(String s, String s1) throws Exception {
    nested.createFileSync(s,s1);
  }

  public void exists(String s, AsyncResultHandler<Boolean> booleanAsyncResultHandler) {
    nested.exists(s,booleanAsyncResultHandler);
  }

  public boolean existsSync(String s) throws Exception {
    return nested.existsSync(s);
  }

  public void fsProps(String s, AsyncResultHandler<FileSystemProps> fileSystemPropsAsyncResultHandler) {
    nested.fsProps(s,fileSystemPropsAsyncResultHandler);
  }

  public FileSystemProps fsPropsSync(String s) throws Exception {
    return nested.fsPropsSync(s);
  }
}
