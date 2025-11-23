package daima.business.rules;

public class ValidationResult<T> {
  private final boolean isInvalid;
  private String message;
  private T data;

  public ValidationResult(String message) {
    this.isInvalid = true;
    this.message = message;
  }

  public ValidationResult(T data) {
    this.isInvalid = false;
    this.data = data;
  }

  public boolean isInvalid() {
    return isInvalid;
  }

  public String getMessage() {
    return message;
  }

  public T getData() {
    return data;
  }
}