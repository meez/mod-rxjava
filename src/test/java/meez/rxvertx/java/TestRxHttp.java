package meez.rxvertx.java;

import org.junit.Test;
import org.vertx.java.testframework.TestBase;
import vertx.tests.rxjava.HttpTestClient;
import vertx.tests.rxjava.JsonServer;
import vertx.tests.rxjava.ProxyServer;

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
  public void testGetNow() throws Exception {
    startTest(getMethodName());
  }

  @Test
  public void testPostJson() throws Exception {
    startApp(JsonServer.class.getName());
    startTest(getMethodName());
  }

  @Test
  public void testParallelUpload() throws Exception {
    startApp(JsonServer.class.getName());
    startTest(getMethodName());
  }

  @Test
  public void testProxyServer() throws Exception {
    startApp(ProxyServer.class.getName());
    startTest(getMethodName());
  }
}
