package org.example.gobookingcommon.customException;

public class CannotVerifyUserException extends RuntimeException {


    public CannotVerifyUserException(String message) {
        super(message);
    }
}
