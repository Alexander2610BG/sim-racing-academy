package aleksandarskachkov.simracingacademy.exception;

public class WalletDoesntBelong extends RuntimeException {
    public WalletDoesntBelong(String message) {
        super(message);
    }

    public WalletDoesntBelong() {
    }
}
