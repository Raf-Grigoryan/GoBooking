package org.example.gobookingcommon.customException;

public class UserOnlyExistException extends RuntimeException {

    public UserOnlyExistException(String message) {
        super(message);
    }
}
