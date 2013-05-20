package meez.rxvertx.java.io;

import meez.rxvertx.java.impl.AsyncResultMemoizeHandler;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.core.file.FileProps;
import org.vertx.java.core.file.FileSystem;
import org.vertx.java.core.file.FileSystemProps;
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
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.copy(s,s1,rh);
    return Observable.create(rh);
  }

  public Observable<Void> copyRx(String s, String s1, boolean b) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.copy(s,s1,b,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> moveRx(String s, String s1) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.move(s,s1,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> truncateRx(String s, long l) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.truncate(s,l,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> chmodRx(String s, String s1) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.chmod(s,s1,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> chmodRx(String s, String s1, String s2) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.chmod(s,s1,s2,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<FileProps> propsRx(String s) {
    AsyncResultMemoizeHandler<FileProps> rh=new AsyncResultMemoizeHandler<FileProps>();
    nested.props(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<FileProps> lpropsRx(String s) {
    AsyncResultMemoizeHandler<FileProps> rh=new AsyncResultMemoizeHandler<FileProps>();
    nested.lprops(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> linkRx(String s, String s1) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.link(s,s1,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> symlinkRx(String s, String s1) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.symlink(s,s1,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> unlinkRx(String s) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.unlink(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<String> readSymlinkRx(String s) {
    AsyncResultMemoizeHandler<String> rh=new AsyncResultMemoizeHandler<String>();
    nested.readSymlink(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> deleteRx(String s) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.delete(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> deleteRx(String s, boolean b) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.delete(s,b,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> mkdirRx(String s) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.mkdir(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> mkdirRx(String s, boolean b) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.mkdir(s,b,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> mkdirRx(String s, String s1) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.mkdir(s,s1,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> mkdirRx(String s, String s1, boolean b) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.mkdir(s,s1,b,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<String[]> readDirRx(String s) {
    AsyncResultMemoizeHandler<String[]> rh=new AsyncResultMemoizeHandler<String[]>();
    nested.readDir(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<String[]> readDirRx(String s, String s1) {
    AsyncResultMemoizeHandler<String[]> rh=new AsyncResultMemoizeHandler<String[]>();
    nested.readDir(s,s1,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Buffer> readFileRx(String s) {
    AsyncResultMemoizeHandler<Buffer> rh=new AsyncResultMemoizeHandler<Buffer>();
    nested.readFile(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> writeFileRx(String s, Buffer buffer) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.writeFile(s,buffer,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<AsyncFile> openRx(String s) {
    AsyncResultMemoizeHandler<AsyncFile> rh=new AsyncResultMemoizeHandler<AsyncFile>();
    nested.open(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<AsyncFile> openRx(String s, String s1) {
    AsyncResultMemoizeHandler<AsyncFile> rh=new AsyncResultMemoizeHandler<AsyncFile>();
    nested.open(s,s1,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<AsyncFile> openRx(String s, String s1, boolean b) {
    AsyncResultMemoizeHandler<AsyncFile> rh=new AsyncResultMemoizeHandler<AsyncFile>();
    nested.open(s,s1,b,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<AsyncFile> openRx(String s, String s1, boolean b, boolean b1, boolean b2) {
    AsyncResultMemoizeHandler<AsyncFile> rh=new AsyncResultMemoizeHandler<AsyncFile>();
    nested.open(s,s1,b,b1,b2,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<AsyncFile> openRx(String s, String s1, boolean b, boolean b1, boolean b2, boolean b3) {
    AsyncResultMemoizeHandler<AsyncFile> rh=new AsyncResultMemoizeHandler<AsyncFile>();
    nested.open(s,s1,b,b1,b2,b3,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> createFileRx(String s) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.createFile(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Void> createFileRx(String s, String s1) {
    AsyncResultMemoizeHandler<Void> rh=new AsyncResultMemoizeHandler<Void>();
    nested.createFile(s,s1,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<Boolean> existsRx(String s) {
    AsyncResultMemoizeHandler<Boolean> rh=new AsyncResultMemoizeHandler<Boolean>();
    nested.exists(s,rh);
    return Observable.create(rh.subscribe);
  }

  public Observable<FileSystemProps> fsPropsRx(String s) {
    AsyncResultMemoizeHandler<FileSystemProps> rh=new AsyncResultMemoizeHandler<FileSystemProps>();
    nested.fsProps(s,rh);
    return Observable.create(rh.subscribe);
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
