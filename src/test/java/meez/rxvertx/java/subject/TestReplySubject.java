package meez.rxvertx.java.subject;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.testframework.TestBase;
import org.vertx.java.testframework.TestUtils;
import rx.util.functions.Action0;
import rx.util.functions.Action1;

/** Unit-test for ReplySubject */
public class TestReplySubject extends TestBase {
  
  private TestUtils tu = new TestUtils(vertx);

  /** Auto-reply */
  public void autoReply(Handler<String> handler) {
    handler.handle("auto-reply");
  }
  
  public Action1<Exception> onTestFailed=new Action1<Exception>() {
    public void call(Exception e) {
      System.err.println("Test failure (e="+e+")");
      e.printStackTrace(System.err);
      tu.exception(e,"test failed");
    }
  };

  // Tests
  
  /** Test multiple replies */
  @Test 
  public void testMultiReply() {
    ReplySubject<String> rx=ReplySubject.create();
    final AtomicInteger replyReceived=new AtomicInteger();
    rx.subscribe(
      new Action1<String>() {
        public void call(String s) {
          System.out.println("Got reply '"+s+"'");
          tu.azzert("first-reply".equals(s));
          replyReceived.addAndGet(1);
        }
      },
      onTestFailed,
      new Action0() {
        public void call() {
          System.out.println("Finished (replies="+replyReceived+")");
          tu.azzert(replyReceived.get()==1);
          tu.testComplete();
        }
      });
    // First 
    rx.handle("first-reply");
    // Additional replies (ignore)
    rx.handle("ignore-me1");
  }

  /** Test synchronous reply (before subscribed) */
  @Test 
  public void testSyncReply() {
    ReplySubject<String> rx=ReplySubject.create();
    final AtomicInteger replyReceived=new AtomicInteger();
    // Reply as part of call
    autoReply(rx);
    // Subscribe after reply
    rx.subscribe(
      new Action1<String>() {
        public void call(String s) {
          System.out.println("Got reply '"+s+"'");
          tu.azzert("auto-reply".equals(s));
          replyReceived.addAndGet(1);
        }
      },
      onTestFailed,
      new Action0() {
        public void call() {
          System.out.println("Finished (replies="+replyReceived+")");
          tu.azzert(replyReceived.get()==1);
          tu.testComplete();
        }
      });
    // Additional replies (ignore)
    rx.handle("ignore-me1");
  }
}
