package vertx.tests.rxjava;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import meez.rxvertx.java.RxSupport;
import meez.rxvertx.java.RxVertx;
import meez.rxvertx.java.io.RxFileSupport;
import meez.rxvertx.java.subject.StreamSubject;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.core.impl.Windows;
import org.vertx.java.testframework.TestClientBase;
import org.vertx.java.testframework.TestUtils;
import rx.util.functions.Action0;
import rx.util.functions.Action1;

/** FileSystem */
public class FileSystem extends TestClientBase {

  private static final String TEST_DIR = "test-tmp";
  private static final String DEFAULT_DIR_PERMS = "rwxr-xr-x";
  private static final String DEFAULT_FILE_PERMS = "rw-r--r--";

  private String pathSep;
  private File testDir;

  @Override
  public void start() {
    super.start();
    java.nio.file.FileSystem fs = FileSystems.getDefault();
    pathSep = fs.getSeparator();

    testDir = new File(TEST_DIR);
    if (testDir.exists()) {
      deleteDir(testDir);
    }
    testDir.mkdir();

    tu.appReady();
  }

  @Override
  public void stop() {
    super.stop();
  }
  
  public Action1<Exception> onTestFailed=new Action1<Exception>() {
    public void call(Exception e) {
      System.err.println("Test failure (e="+e+")");
      e.printStackTrace(System.err);
      tu.exception(e,"test failed");
    }
  };

  // Tests
  
  public void testWriteStream() throws Exception {
    final String fileName = "some-file.dat";
    final int chunkSize = 1000;
    final int chunks = 10;
    byte[] content = TestUtils.generateRandomByteArray(chunkSize * chunks);
    final Buffer buff = new Buffer(content);
    
    new RxVertx(vertx).fileSystem().openRx(TEST_DIR + pathSep + fileName)
      .subscribe(new Action1<AsyncFile>() {
        public void call(final AsyncFile file) {

          StreamSubject<Buffer> out=StreamSubject.create();

          RxSupport
            .stream(out,file.getWriteStream())
            .subscribe(
              new Action1<Long>() {
                public void call(Long bytesWritten) {
                  System.out.println("Wrote "+bytesWritten+" byte(s)");
                }
              },
              onTestFailed
            );
          
          // Write all the buffer to the stream
          for (int i = 0; i < chunks; i++) {
            Buffer chunk = buff.getBuffer(i * chunkSize, (i + 1) * chunkSize);
            tu.azzert(chunk.length() == chunkSize);
            out.onNext(chunk);
          }
          out.onCompleted();
          
          System.out.println("Test complete. Closing file");

          RxFileSupport.closeRx(file)
            .subscribe(
              new Action1<Void>() {
                public void call(Void v) {
                  tu.azzert(fileExists(fileName));
                  byte[] readBytes;
                  try {
                    readBytes = Files.readAllBytes(Paths.get(TEST_DIR + pathSep + fileName));
                  } catch (IOException e) {
                    tu.exception(e, "failed to read");
                    return;
                  }
                  tu.azzert(TestUtils.buffersEqual(buff, new Buffer(readBytes)));
                  tu.testComplete();
                }
              },
              onTestFailed
            );
        }
      });
  }
  
  public void testReadStream() throws Exception {
    final String fileName = "some-file.dat";
    final int chunkSize = 1000;
    final int chunks = 10;
    final byte[] content = TestUtils.generateRandomByteArray(chunkSize * chunks);
    createFile(fileName, content);

    final Buffer res=new Buffer();
    new RxVertx(vertx).fileSystem().openRx(TEST_DIR + pathSep + fileName, null, true, false, false)
      .subscribe(new Action1<AsyncFile>() {
        public void call(final AsyncFile file) {
          RxSupport.toObservable(file.getReadStream())
            .subscribe(
              new Action1<Buffer>() {
                public void call(Buffer data) {
                  tu.checkContext();
                  res.appendBuffer(data);
                }
              },
              onTestFailed,
              new Action0() {
                public void call() {
                  System.out.println("Test complete. Closing file");
                  RxFileSupport.closeRx(file)
                    .subscribe(new Action1<Void>() {
                      public void call(Void aVoid) {
                        System.out.println("Test complete. Closing file");
                        tu.checkContext();
                        tu.azzert(TestUtils.buffersEqual(res,new Buffer(content)));
                        tu.testComplete();
                      }
                    },onTestFailed);
                }
              });
        }
      },
      onTestFailed);
  }
  
  // Helper methods

  private boolean fileExists(String fileName) {
    File file = new File(testDir, fileName);
    return file.exists();
  }

  private void createFileWithJunk(String fileName, long length) throws Exception {
    createFile(fileName, TestUtils.generateRandomByteArray((int) length));
  }

  private void createFile(String fileName, byte[] bytes) throws Exception {
    File file = new File(testDir, fileName);
    Path path = Paths.get(file.getCanonicalPath());
    Files.write(path, bytes);
    
    setPerms( path, DEFAULT_FILE_PERMS );
  }

  private void deleteDir(File dir) {
    File[] files = dir.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        deleteDir(files[i]);
      } else {
        files[i].delete();
      }
    }
    dir.delete();
  }

  private void deleteDir(String dir) {
    deleteDir(new File(TEST_DIR + pathSep + dir));
  }

  private void mkDir(String dirName) throws Exception {
    File dir = new File(TEST_DIR + pathSep + dirName);
    dir.mkdir();
    
    setPerms( Paths.get( dir.getCanonicalPath() ), DEFAULT_DIR_PERMS );
  }

  private long fileLength(String fileName) {
    File file = new File(testDir, fileName);
    return file.length();
  }

  private void setPerms(Path path, String perms) {
  	if (Windows.isWindows() == false) {
	    try {
	      Files.setPosixFilePermissions( path, PosixFilePermissions.fromString( perms ) );
	    }
	    catch(IOException e) { 
	      throw new RuntimeException(e.getMessage());
	    } 
	  }
  }
  
  private String getPerms(String fileName) {
    try {
      Set<PosixFilePermission> perms = Files.getPosixFilePermissions(Paths.get(testDir + pathSep + fileName));
      return PosixFilePermissions.toString(perms);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private void deleteFile(String fileName) {
    File file = new File(TEST_DIR + pathSep + fileName);
    file.delete();
  }
}
