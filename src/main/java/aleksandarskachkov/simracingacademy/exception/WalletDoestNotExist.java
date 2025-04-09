package aleksandarskachkov.simracingacademy.exception;

public class WalletDoestNotExist extends RuntimeException {
    public WalletDoestNotExist(String message) {
        super(message);
    }

    public WalletDoestNotExist() {
    }
}
