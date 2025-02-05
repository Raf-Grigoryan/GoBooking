package org.example.gobooking.customException;

public class CardCountException extends RuntimeException {
    public CardCountException(String message) {
        super(message);
    }
}
