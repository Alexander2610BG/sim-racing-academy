package aleksandarskachkov.simracingacademy.exception;

public class UserDoesntOwnModule extends RuntimeException {
    public UserDoesntOwnModule(String message) {
        super(message);
    }

  public UserDoesntOwnModule() {
  }
}
