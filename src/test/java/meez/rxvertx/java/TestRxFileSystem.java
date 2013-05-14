package meez.rxvertx.java;

import org.junit.Test;
import org.vertx.java.testframework.TestBase;
import vertx.tests.rxjava.FileSystem;

/** Unit-test for RxFileSystem */
public class TestRxFileSystem extends TestBase {
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    startApp(FileSystem.class.getName());
  }
  
  // Functional tests
  
  @Test 
  public void testWriteStream() {
    startTest(getMethodName());
  }
  
  @Test 
  public void testReadStream() {
    startTest(getMethodName());
  }
}
