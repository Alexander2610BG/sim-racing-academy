package aleksandarskachkov.simracingacademy.exception;

public class ModuleNotFound extends RuntimeException {
    public ModuleNotFound(String message) {
        super(message);
    }

    public ModuleNotFound() {
    }
}
