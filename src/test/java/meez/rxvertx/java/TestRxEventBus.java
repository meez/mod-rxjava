package meez.rxvertx.java;

import org.junit.Test;
import org.vertx.java.testframework.TestBase;
import vertx.tests.rxjava.EventBus;

/** Unit-test RxEventBus */
public class TestRxEventBus extends TestBase {
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    startApp(EventBus.class.getName());
  }
  
  // Functional tests

  @Test
  public void testSend() throws Exception {
    startTest(getMethodName());
  }

  @Test
  public void testSendRx() throws Exception {
    startTest(getMethodName());
  }

  @Test
  public void testToObservableLocal() throws Exception {
    startTest(getMethodName());
  }

  // Integration / Examples
  
  @Test
  public void testEventBusPipeline() throws Exception {
    startTest(getMethodName());
  }
}
