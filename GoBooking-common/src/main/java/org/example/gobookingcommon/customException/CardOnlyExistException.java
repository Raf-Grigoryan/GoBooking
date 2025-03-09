package org.example.gobookingcommon.customException;

public class CardOnlyExistException extends RuntimeException {
    public CardOnlyExistException(String message) {
        super(message);
    }
}
