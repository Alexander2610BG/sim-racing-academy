package aleksandarskachkov.simracingacademy.exception;

public class NoActiveSubscriptionFound extends RuntimeException {
    public NoActiveSubscriptionFound(String message) {
        super(message);
    }

    public NoActiveSubscriptionFound() {
    }
}
