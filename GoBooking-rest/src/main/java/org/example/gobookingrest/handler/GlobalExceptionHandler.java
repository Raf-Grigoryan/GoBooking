package org.example.gobookingrest.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.customException.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot log in");
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<String> passwordIncorrectException(PasswordIncorrectException ex) {
        log.warn("Password incorrect: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGeneralException(Exception ex) {
//        log.error(ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
//    }

    @ExceptionHandler(UserOnlyExistException.class)
    public ResponseEntity<String> handleUserOnlyExistException(UserOnlyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundException(UserNotFoundException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AccountNotVerifiedException.class)
    public ResponseEntity<String> accountNotVerifiedException(AccountNotVerifiedException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CannotVerifyUserException.class)
    public ResponseEntity<String> cannotVerifyUserException(CannotVerifyUserException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CardCountException.class)
    public ResponseEntity<String> cardCountException(CardCountException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CardOnlyExistException.class)
    public ResponseEntity<String> cardOnlyExistException(CardOnlyExistException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedCardAccessException.class)
    public ResponseEntity<String> unauthorizedCardAccessException(UnauthorizedCardAccessException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedWorkGraphicModificationException.class)
    public ResponseEntity<String> unauthorizedWorkGraphicModificationException(UnauthorizedWorkGraphicModificationException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}