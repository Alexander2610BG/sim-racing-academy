package aleksandarskachkov.simracingacademy.exception;

public class UserDoesntOwnTrack extends RuntimeException {
    public UserDoesntOwnTrack(String message) {
        super(message);
    }

  public UserDoesntOwnTrack() {
  }
}
