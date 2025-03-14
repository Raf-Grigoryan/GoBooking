package org.example.gobookingrest.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.gobookingcommon.customException.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.xml.bind.ValidationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<String> passwordIncorrectException(PasswordIncorrectException ex) {
        log.warn("Password incorrect: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
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
    public ResponseEntity<String> userNotFoundException(UserNotFoundException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AccountNotVerifiedException.class)
    public ResponseEntity<String> accountNotVerifiedException(AccountNotVerifiedException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
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

    @ExceptionHandler(UnauthorizedCardAccessException.class)
    public ResponseEntity<String> unauthorizedCardAccessException(UnauthorizedCardAccessException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedWorkGraphicModificationException.class)
    public ResponseEntity<String> unauthorizedWorkGraphicModificationException(UnauthorizedWorkGraphicModificationException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(AddressOnlyExistException.class)
    public ResponseEntity<String> addressOnlyExistException(AddressOnlyExistException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(AlreadyRoleChangeRequestException.class)
    public ResponseEntity<String> alreadyRoleChangeRequestException(AlreadyRoleChangeRequestException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }


    @ExceptionHandler(CardOnlyExistException.class)
    public ResponseEntity<String> cardOnlyExistException(CardOnlyExistException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CompanyAlreadyExistsException.class)
    public ResponseEntity<String> companyAlreadyExistsException(CompanyAlreadyExistsException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> insufficientFundsException(InsufficientFundsException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(SubscriptionOnlyExistException.class)
    public ResponseEntity<String> subscriptionOnlyExistException(SubscriptionOnlyExistException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<String> invalidRoleException(InvalidRoleException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<String> companyNotFoundException(CompanyNotFoundException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UsersMismatchException.class)
    public ResponseEntity<String> usersMismatchException(UsersMismatchException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TypeNotExistException.class)
    public ResponseEntity<String> usersMismatchException(TypeNotExistException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(SlotAlreadyBookedException.class)
    public ResponseEntity<String> slotAlreadyBookedException(SlotAlreadyBookedException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> validationException(ValidationException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ConfirmPasswordIncorrectException.class)
    public ResponseEntity<String> confirmPasswordIncorrectException(ConfirmPasswordIncorrectException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AlreadyDirectorRequestedException.class)
    public ResponseEntity<String> alreadyDirectorRequestedException(AlreadyDirectorRequestedException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentException(IllegalArgumentException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedServiceDeletionException.class)
    public ResponseEntity<String> unauthorizedServiceDeletionException(UnauthorizedServiceDeletionException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(DirectorNotMatchException.class)
    public ResponseEntity<String> directorNotMatchException(DirectorNotMatchException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(SubscriptionNotValidException.class)
    public ResponseEntity<String> subscriptionNotValidException(SubscriptionNotValidException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(CardNotExistException.class)
    public ResponseEntity<String> cardNotExistException(CardNotExistException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

   @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeException(RuntimeException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }



}