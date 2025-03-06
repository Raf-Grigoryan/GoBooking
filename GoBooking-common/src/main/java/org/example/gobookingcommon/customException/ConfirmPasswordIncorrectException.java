package org.example.gobookingcommon.customException;

public class ConfirmPasswordIncorrectException extends RuntimeException {
    public ConfirmPasswordIncorrectException(String message) {
        super(message);
    }
}
