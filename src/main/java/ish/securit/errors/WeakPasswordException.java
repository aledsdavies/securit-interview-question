package ish.securit.errors;

public class WeakPasswordException extends Exception {
    public WeakPasswordException(String message) {
        super(message);
    }
}
