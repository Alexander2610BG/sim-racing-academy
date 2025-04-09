package aleksandarskachkov.simracingacademy.exception;

public class TrackNotFound extends RuntimeException {
    public TrackNotFound(String message) {
        super(message);
    }

    public TrackNotFound() {
    }
}
