package org.example.gobookingcommon.customException;

public class SubscriptionOnlyExistException extends RuntimeException {
    public SubscriptionOnlyExistException(String message) {
        super(message);
    }
}
