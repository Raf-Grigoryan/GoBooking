package org.example.gobooking.customException;

public class UserOnlyExistException extends RuntimeException {

    public UserOnlyExistException(String message) {
        super(message);
    }
}
