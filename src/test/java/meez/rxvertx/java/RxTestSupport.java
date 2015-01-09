package meez.rxvertx.java;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import org.vertx.java.testframework.TestUtils;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/** RxTestSupport */
public class RxTestSupport {

  // Map tracing
  
  public static <T> Func1<T,T> traceMap(final String tag) {
    return new Func1<T,T>() {
      public T call(T v) {
        System.out.println("MAP "+tag+" "+v);
        return v;
      }
    };
  }
  
  // Subscribe tracing
  
  public static <T> Action1<T> traceValue(final String tag) {
    return new Action1<T>() {
      public void call(T v) {
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
  
  // List
  
  /** Convert elements into a mutable List */
  public static List toList(Object... a) {
    List<Object> res=new ArrayList<Object>(a.length);
    for (int i=0;i<a.length;i++) {
      res.add(a[i]);
    }
    return res;
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

  public static Action1<Throwable> testFailed(final TestUtils tu) {
    return new Action1<Throwable>() {
      public void call(Throwable e) {
        System.err.println("Test failed: "+e);
        e.printStackTrace(System.err);
        tu.azzert(false);
      }
    };
  }

  public static Action1<Exception> expectedFailure(final TestUtils tu, final Class expType) {
    return new Action1<Exception>() {
      @SuppressWarnings(value="unchecked")
      public void call(Exception e  ) {
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
  
  /** Observer that expects a list */
  public static <T> Observer<T> assertValues(final TestUtils tu, final T[] exp, final CountDownLatch latch) {
    return new Observer<T>() {
      private int idx=0;
      public void onNext(T value) {
        tu.azzert(idx<exp.length,"Too many results");
        tu.azzert(exp[idx].equals(value),"Match result");
        System.out.println("Got value: "+value);
        idx++;
      }
      public void onCompleted() {
        tu.azzert(idx==exp.length,"onCompleted() with values remaining");
        latch.countDown();
        // Call max once
        idx++;
      }
      public void onError(Throwable e) {
        // Call never
        tu.azzert(false,"Unexpected error (e="+e+")");
        latch.countDown();
      }
    };
  }

  /** Observer that expects a single value */
  public static <T> Observer<T> assertSingleValue(final TestUtils tu, final T exp, final CountDownLatch latch) {
    return new Observer<T>() {
      private int count=0;
      public void onNext(T value) {
        tu.azzert(count==0,"Single result");
        tu.azzert(exp.equals(value),"Match result");
        System.out.println("Got value: "+value);
        count++;
      }
      public void onCompleted() {
        tu.azzert(count==1,"onCompleted() with no value");
        latch.countDown();
      }
      public void onError(Throwable e) {
        tu.azzert(false,"Unexpected error (e="+e+")");
        latch.countDown();
      }
    };
  }

  /** Observer that expects an error of a given type */
  public static <T> Observer<T> assertError(final TestUtils tu, final Class errType) {
    return new Observer<T>() {
      public void onNext(T value) {
        System.out.println("Got value: "+value);
      }
      public void onCompleted() {
        tu.azzert(false,"Error expected");
      }
      @SuppressWarnings("unchecked")
      public void onError(Throwable e) {
        tu.azzert(errType.isAssignableFrom(e.getClass()));
        System.out.println("Expected error: "+e);
        tu.testComplete();
      }
    };
  }
}
