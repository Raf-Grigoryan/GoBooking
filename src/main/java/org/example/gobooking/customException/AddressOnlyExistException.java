package org.example.gobooking.customException;

public class AddressOnlyExistException extends RuntimeException {
    public AddressOnlyExistException(String message) {
        super(message);
    }
}
