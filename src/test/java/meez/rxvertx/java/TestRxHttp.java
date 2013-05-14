package meez.rxvertx.java;

import org.junit.Test;
import org.vertx.java.testframework.TestBase;
import vertx.tests.rxjava.HttpTestClient;
import vertx.tests.rxjava.JsonServer;

/**
 * TestRxHttp
 */
public class TestRxHttp extends TestBase {
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    startApp(HttpTestClient.class.getName());
  }
  
  // Tests
  
  @Test
  public void testPostJsonRx() throws Exception {
    startApp(JsonServer.class.getName());
    startTest(getMethodName());
  }
}
