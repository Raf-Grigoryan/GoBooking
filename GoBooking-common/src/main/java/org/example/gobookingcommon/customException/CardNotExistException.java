package org.example.gobookingcommon.customException;

public class CardNotExistException extends RuntimeException {
    public CardNotExistException(String message) {
        super(message);
    }
}
