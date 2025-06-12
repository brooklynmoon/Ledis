package wtf.ledis.exception;

public class WriterException extends RuntimeException {
    public WriterException(String message) {
        super(message);
    }

  public WriterException(String message, Throwable cause) {
    super(message, cause);
  }

  public WriterException(Throwable cause) {
    super(cause);
  }

  public WriterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public WriterException() {
  }
}
