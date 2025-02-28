package org.example.gobookingcommon.customException;

public class AlreadyRoleChangeRequestException extends RuntimeException {
    public AlreadyRoleChangeRequestException(String message) {
        super(message);
    }
}
