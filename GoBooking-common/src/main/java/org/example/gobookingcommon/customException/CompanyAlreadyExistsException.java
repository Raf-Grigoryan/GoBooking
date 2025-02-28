package org.example.gobookingcommon.customException;

public class CompanyAlreadyExistsException extends RuntimeException {
    public CompanyAlreadyExistsException(String message) {
        super(message);
    }
}
