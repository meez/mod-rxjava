package vertx.tests.rxjava;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import meez.rxvertx.java.RxTimer;
import org.vertx.java.testframework.TestClientBase;
import rx.functions.Action1;

import static meez.rxvertx.java.RxTestSupport.*;

/**
 * Timer
 */
public class Timer extends TestClientBase {

  private RxTimer timer;

  @Override
  public void start() {
    super.start();
    tu.appReady();
    timer=new RxTimer(vertx);
  }
  
  // Tests
  
  public void testAfter() throws Exception {
    
    timer
      .after(500,"ok")
      .subscribe(testValue(tu,"ok"),testFailed(tu),testComplete(tu));
  }

  public void testEvery() throws Exception {
    
    final CountDownLatch latch=new CountDownLatch(4);
    
    final long st=System.currentTimeMillis();
    final AtomicLong check=new AtomicLong(st);
    final AtomicLong counter=new AtomicLong(4);

    timer
      .every(500,"ok")
      .subscribe(new Action1<String>() {
        public void call(String s) {
          long now=System.currentTimeMillis();
          long tt=now-check.get();
          
          System.out.println("["+counter.get()+"] '"+s+"' ("+tt+"ms)");
          
          tu.azzert("ok".equals(s));
          tu.azzert(Math.abs(500-tt)<50);
          
          check.set(now);
          if (counter.addAndGet(-1)==0) {
            tu.testComplete();
          }
        }
      },testFailed(tu));
  }
  
  
}