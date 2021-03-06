package ar.edu.itba.paw.models.exception;

public class AnswerNotFoundException extends RuntimeException {

    public AnswerNotFoundException() {
        super("Answer does not exist");
    }

    public AnswerNotFoundException(String message) {
        super(message);
    }

    public AnswerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnswerNotFoundException(Throwable cause) {
        super(cause);
    }

    public AnswerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
