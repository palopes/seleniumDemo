package be.ap.config;

public class SecurityException extends Exception {

    public SecurityException(final String message) {
        super(message);
    }

    public SecurityException(final Throwable cause) {
        super(cause);
    }

    public SecurityException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
