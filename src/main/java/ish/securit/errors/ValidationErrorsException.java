package ish.securit.errors;

import lombok.Getter;

import java.util.Map;


@Getter
public class ValidationErrorsException extends Exception {
    private Map<String, String> errors;

    public ValidationErrorsException(Map<String, String> errors) {
        super("Validation Errors while processing your request");
        this.errors = errors;
    }
}
