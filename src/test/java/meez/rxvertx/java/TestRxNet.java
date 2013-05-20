package meez.rxvertx.java;

import org.junit.Test;
import org.vertx.java.testframework.TestBase;
import vertx.tests.rxjava.NetTestClient;

/** TestRxNet */
public class TestRxNet extends TestBase {
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    startApp(NetTestClient.class.getName());
  }
  
  // Tests
  
  @Test
  public void testSimpleServer() throws Exception {
    startTest(getMethodName());
  }

}
