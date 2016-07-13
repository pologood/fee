package commons.saas;

public class PayException extends RuntimeException {
  public PayException(String msg) {
    super(msg);
  }
  public PayException(Throwable cause) {
    super(cause);
  }
}
