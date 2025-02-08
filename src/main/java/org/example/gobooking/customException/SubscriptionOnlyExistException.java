package org.example.gobooking.customException;

public class SubscriptionOnlyExistException extends RuntimeException {
    public SubscriptionOnlyExistException(String message) {
        super(message);
    }
}
