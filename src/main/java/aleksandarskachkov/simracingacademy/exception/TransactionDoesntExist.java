package aleksandarskachkov.simracingacademy.exception;

public class TransactionDoesntExist extends RuntimeException {
    public TransactionDoesntExist(String message) {
        super(message);
    }

    public TransactionDoesntExist() {
    }
}
