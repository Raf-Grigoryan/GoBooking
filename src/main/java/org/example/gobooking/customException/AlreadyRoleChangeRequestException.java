package org.example.gobooking.customException;

public class AlreadyRoleChangeRequestException extends RuntimeException {
    public AlreadyRoleChangeRequestException(String message) {
        super(message);
    }
}
