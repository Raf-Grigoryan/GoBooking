package org.example.gobookingcommon.customException;

public class CardCountException extends RuntimeException {
    public CardCountException(String message) {
        super(message);
    }
}
