package meez.rxvertx.java.subject;

import meez.rxvertx.java.RxTestSupport;
import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.testframework.TestBase;
import org.vertx.java.testframework.TestUtils;
import rx.Observable;
import rx.util.functions.Action0;
import rx.util.functions.Action1;
import rx.util.functions.Func1;
import rx.util.functions.Func2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/** Unit-test for ReplySubject */
public class TestReplySubject extends TestBase {
  
  private TestUtils tu = new TestUtils(vertx);

  /** Auto-reply */
  public void autoReply(Handler<String> handler) {
    handler.handle("auto-reply");
  }
  
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
      RxTestSupport.testFailed(tu),
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
      RxTestSupport.testFailed(tu),
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
  
  /** Test multiple subscribers (before after reply) */
  @Test 
  public void testMultiSubscriber() throws InterruptedException {
    ReplySubject<String> rx=ReplySubject.create();
    final CountDownLatch latch=new CountDownLatch(2); 

    // Subscribe before handle()
    rx.subscribe(
      RxTestSupport.testValue(tu,new String[] {"reply"},latch),
      RxTestSupport.testFailed(tu));

    System.out.println("Sending reply");
    
    // Send reply 
    rx.handle("reply");

    // Subscribe after handle()
    rx.subscribe(
      RxTestSupport.testValue(tu,new String[] {"reply"},latch),
      RxTestSupport.testFailed(tu));
    
    latch.await();
    
    tu.testComplete();
  }
  
  /** Test cascading handlers */
  @Test 
  public void testCascading() throws InterruptedException {
    ReplySubject<Long> a1=ReplySubject.create();
    ReplySubject<Long> b1=ReplySubject.create();
    
    vertx.setTimer(100,a1);
    vertx.setTimer(100,b1);
    
    // Create two pipelines, replysubject creates new replysubject
    
    Observable<Long> txa=
      a1
        .mapMany(new Func1<Long, Observable<Long>>() {
          public Observable<Long> call(Long aLong) {
            System.out.println("a1 -> start a2");          
            ReplySubject<Long> a2=ReplySubject.create();
            vertx.setTimer(100,a2);
            return a2; 
          }
        });
    
    Observable<Long> txb=
      b1
        .mapMany(new Func1<Long, Observable<Long>>() {
          public Observable<Long> call(Long aLong) {
            System.out.println("b1 -> start b2");          
            ReplySubject<Long> b2=ReplySubject.create();
            vertx.setTimer(100,b2);
            return b2; 
          }
        });
    
    // Subscribe to both
    txa.subscribe(RxTestSupport.traceValue("txa"));
    txb.subscribe(RxTestSupport.traceValue("txb"));
    
    final CountDownLatch latch=new CountDownLatch(1); 
    
    // Also zip values
    Observable
      .zip(txa,txb,
        new Func2<Long,Long,Long>() {
          public Long call(Long a, Long b) {
            System.out.println("zip(a+b)="+a+"+"+b+"="+(a+b));
            return a+b; 
          }
        })
      .subscribe(
        new Action1<Long>() {
          public void call(Long value) {
            System.out.println("finished="+value);
            latch.countDown();
          }
        },
        RxTestSupport.testFailed(tu),
        RxTestSupport.testComplete(tu)
      );
    
    latch.await();
    
    tu.testComplete();
  }
}
