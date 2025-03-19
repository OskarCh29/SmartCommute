package pl.smartCommute.app.exception;

public class RecordExistsException extends RuntimeException {
    public RecordExistsException(String message) {
        super(message);
    }
}
