package org.example.gobooking.customException;

public class CardOnlyExistException extends RuntimeException {
    public CardOnlyExistException(String message) {
        super(message);
    }
}
