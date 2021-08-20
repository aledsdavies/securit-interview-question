package ish.securit.controllers;

import ish.securit.dtos.ErrorMessage;
import ish.securit.errors.MalformedIdException;
import ish.securit.errors.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralControllerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundExceptions(NotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .message("Requested safebox does not exist")
                .ex(ex)
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MalformedIdException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleValidationExceptions(Exception ex) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .message("Malformed expected data")
                .ex(ex)
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
