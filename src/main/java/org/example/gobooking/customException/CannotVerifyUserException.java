package org.example.gobooking.customException;

public class CannotVerifyUserException extends RuntimeException {


    public CannotVerifyUserException(String message) {
        super(message);
    }
}
