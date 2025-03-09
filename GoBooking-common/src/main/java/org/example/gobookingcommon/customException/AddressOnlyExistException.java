package org.example.gobookingcommon.customException;

public class AddressOnlyExistException extends RuntimeException {
    public AddressOnlyExistException(String message) {
        super(message);
    }
}
