package org.example.gobookingcommon.customException;

public class SubscriptionNotValidException extends RuntimeException {
    public SubscriptionNotValidException(String message) {
        super(message);
    }
}
