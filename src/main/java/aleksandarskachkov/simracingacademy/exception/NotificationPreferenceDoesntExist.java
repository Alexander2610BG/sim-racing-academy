package aleksandarskachkov.simracingacademy.exception;

public class NotificationPreferenceDoesntExist extends RuntimeException {
    public NotificationPreferenceDoesntExist(String message) {
        super(message);
    }

    public NotificationPreferenceDoesntExist() {
    }
}
