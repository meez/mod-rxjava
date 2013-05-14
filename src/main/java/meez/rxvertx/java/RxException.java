package meez.rxvertx.java;

/** All Functions in subscriber chain can only throw RuntimeException so we provide this class to 
 * provide additional context **/
public class RxException extends RuntimeException {

  /** Create new RxException */
  public RxException(String msg) {
    super(msg);
  }
  
  /** Create new RxException that wraps an exception */
  public RxException(String msg, Throwable cause) {
    super(msg,cause);
  }
}
