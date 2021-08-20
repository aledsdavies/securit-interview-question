package ish.securit.controllers;

import ish.securit.dtos.ErrorMessage;
import ish.securit.errors.MalformedIdException;
import ish.securit.errors.NotFoundException;
import ish.securit.errors.SafeboxExistsException;
import ish.securit.errors.ValidationErrorsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GeneralControllerAdvice {
    @ExceptionHandler(SafeboxExistsException.class)
    public ResponseEntity<ErrorMessage> handleSafeboxExistsExceptions(SafeboxExistsException ex) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .message("Safebox already exists")
                .ex(ex)
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundExceptions(NotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .message("Requested safebox does not exist")
                .ex(ex)
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MalformedIdException.class})
    public ResponseEntity<ErrorMessage> handleInvalidIdExceptions(MalformedIdException ex) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .message("Malformed expected data")
                .ex(ex)
                .build(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleSpringValidationExceptions(MethodArgumentNotValidException ex) {
        /*
            Process the list of validation errors returned to the MethodArgumentNotValidException into a serializable
            format to be consumed in the JSON response.
         */
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(ErrorMessage.builder()
                .message("Malformed expected data")
                .ex(new ValidationErrorsException(errors))
                .build(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneralExceptions(Exception ex) {

        return new ResponseEntity<>(ErrorMessage.builder()
                .message("Unexpected API error")
                .ex(ex)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
