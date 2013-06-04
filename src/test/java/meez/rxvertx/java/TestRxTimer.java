package meez.rxvertx.java;

import org.junit.Test;
import org.vertx.java.testframework.TestBase;
import vertx.tests.rxjava.Timer;

/** Unit-test for RxTimer */
public class TestRxTimer extends TestBase {
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    startApp(Timer.class.getName());
  }
  
  // Tests
  
  @Test
  public void testAfter() throws Exception {
    startTest(getMethodName());
  }
  
  @Test
  public void testEvery() throws Exception {
    startTest(getMethodName());
  }
}
