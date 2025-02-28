package org.example.gobookingrest.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.customException.AccountNotVerifiedException;
import org.example.gobookingcommon.customException.PasswordIncorrectException;
import org.example.gobookingcommon.customException.UserNotFoundException;
import org.example.gobookingcommon.customException.UserOnlyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot log in");
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<String> handleIllegalArgument(PasswordIncorrectException ex) {
        log.warn("Password incorrect: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot log in");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }

    @ExceptionHandler(UserOnlyExistException.class)
    public ResponseEntity<String> handleUserOnlyExistException(UserOnlyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> UserNotFountException(UserNotFoundException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AccountNotVerifiedException.class)
    public ResponseEntity<String> AccountNotVerifiedException(AccountNotVerifiedException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


}
