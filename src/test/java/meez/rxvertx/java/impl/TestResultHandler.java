package meez.rxvertx.java.impl;

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

/**
 * TestResultHandler
 */
public class TestResultHandler extends TestBase {
  
  private TestUtils tu = new TestUtils(vertx);

  /** Auto-reply */
  public void autoReply(Handler<String> handler) {
    handler.handle("auto-reply");
  }
  
  // Tests
  
  /** Test multiple replies */
  @Test 
  public void testMultiReply() {
    ResultMemoizeHandler<String> rh=new ResultMemoizeHandler<String>();
    Observable<String> rx=Observable.create(rh.subscribe);
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
    rh.handle("first-reply");
    // Additional replies (ignore)
    rh.handle("ignore-me1");
  }

  /** Test synchronous reply (before subscribed) */
  @Test 
  public void testSyncReply() {
    ResultMemoizeHandler<String> rh=new ResultMemoizeHandler<String>();
    Observable<String> rx=Observable.create(rh.subscribe);
    final AtomicInteger replyReceived=new AtomicInteger();
    // Reply as part of call
    autoReply(rh);
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
    rh.handle("ignore-me1");
  }
  
  /** Multiple subscribers not supported -> Test multiple subscribers (before after reply) */
  public void notestMultiSubscriber() throws InterruptedException {
    ResultMemoizeHandler<String> rh=new ResultMemoizeHandler<String>();
    Observable<String> rx=Observable.create(rh.subscribe);
    final CountDownLatch latch=new CountDownLatch(2); 

    // Subscribe before handle()
    rx.subscribe(
      RxTestSupport.testValue(tu,new String[] {"reply"},latch),
      RxTestSupport.testFailed(tu));

    System.out.println("Sending reply");
    
    // Send reply 
    rh.handle("reply");

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
    ResultMemoizeHandler<Long> a1h=new ResultMemoizeHandler<Long>();
    ResultMemoizeHandler<Long> b1h=new ResultMemoizeHandler<Long>();
    Observable<Long> a1=Observable.create(a1h.subscribe);
    Observable<Long> b1=Observable.create(b1h.subscribe);
    
    vertx.setTimer(100,a1h);
    vertx.setTimer(100,b1h);
    
    // Create two pipelines, replysubject creates new replysubject
    
    Observable<Long> txa=
      a1
        .mapMany(new Func1<Long, Observable<Long>>() {
          public Observable<Long> call(Long aLong) {
            System.out.println("a1 -> start a2");          
            ResultMemoizeHandler<Long> a2h=new ResultMemoizeHandler<Long>();
            Observable<Long> a2=Observable.create(a2h.subscribe);
            vertx.setTimer(100,a2h);
            return a2; 
          }
        });
    
    Observable<Long> txb=
      b1
        .mapMany(new Func1<Long, Observable<Long>>() {
          public Observable<Long> call(Long aLong) {
            System.out.println("b1 -> start b2");          
            ResultMemoizeHandler<Long> b2h=new ResultMemoizeHandler<Long>();
            Observable<Long> b2=Observable.create(b2h.subscribe);
            vertx.setTimer(100,b2h);
            return b2; 
          }
        });
    
    // Subscribe to both
    //txa.subscribe(RxTestSupport.traceValue("txa"));
    //txb.subscribe(RxTestSupport.traceValue("txb"));
    
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
