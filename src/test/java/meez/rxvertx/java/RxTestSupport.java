package meez.rxvertx.java;

import org.vertx.java.testframework.TestUtils;
import rx.util.functions.Action0;
import rx.util.functions.Action1;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

/** RxTestSupport */
public class RxTestSupport {
  
  // Subscribe tracing
  
  public static Action1 traceValue(final String tag) {
    return new Action1() {
      public void call(Object v) {
        System.out.println("NEXT "+tag+" "+v+" ["+(v!=null?v.getClass():"-null-")+"]");
      }
    };
  }

  public static Action1<Exception> traceError(final String tag) {
    return new Action1<Exception>() {
      public void call(Exception e) {
        System.err.println("ERROR "+tag+e);
        e.printStackTrace(System.err);
      }
    };
  }
  
  public static Action0 traceComplete(final String tag) {
    return new Action0() {
      public void call() {
        System.out.println("COMPLETE "+tag);
      }
    };
  }
  
  // Azzert

  public static <T> Action1<T> testValue(final TestUtils tu, T value) {
    return testValue(tu, Collections.singletonList(value).iterator());
  }

  public static <T> Action1<T> testValue(final TestUtils tu, final T[] exp) {
    return testValue(tu, Arrays.asList(exp).iterator());
  }

  public static <T> Action1<T> testValue(final TestUtils tu, final Iterator<T> exp) {
    return new Action1<T>() {
      public void call(T t) {
        tu.azzert(exp.hasNext());
        
        T value=exp.next();
        System.out.println("onNext:"+value);
        
        tu.azzert(value.equals(t),"Expected '"+value+"' got '"+t+"'");
      }
    };
  }
  
  public static <T> Action1<T> testValue(final TestUtils tu, T exp, final CountDownLatch latch) {
    return testValue(tu, Collections.singletonList(exp).iterator(), latch);
  }

  public static <T> Action1<T> testValue(final TestUtils tu, final T[] exp, final CountDownLatch latch) {
    return testValue(tu, Arrays.asList(exp).iterator(),latch);
  }

  public static <T> Action1<T> testValue(final TestUtils tu, final Iterator<T> exp, final CountDownLatch latch) {
    return new Action1<T>() {
      public void call(T t) {
        tu.azzert(exp.hasNext());
        
        T value=exp.next();
        System.out.println("onNext:"+value);
        
        tu.azzert(value.equals(t));
        
        latch.countDown();
      }
    };
  }

  public static Action1<Exception> testFailed(final TestUtils tu) {
    return new Action1<Exception>() {
      public void call(Exception e) {
        System.err.println("Test failed: "+e);
        e.printStackTrace(System.err);
        tu.azzert(false);
      }
    };
  }

  public static Action1<Exception> expectedFailure(final TestUtils tu, final Class expType) {
    return new Action1<Exception>() {
      public void call(Exception e) {
        tu.azzert(expType.isAssignableFrom(e.getClass()));
        System.out.println("Expected error: "+e);
        tu.testComplete();
      }
    };
  }

  public static Action0 testComplete(final TestUtils tu) {
    return new Action0() {
      public void call() {
        tu.testComplete();
      }
    };
  }
}
