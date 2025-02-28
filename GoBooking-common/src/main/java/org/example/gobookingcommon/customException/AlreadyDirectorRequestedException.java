package org.example.gobookingcommon.customException;

public class AlreadyDirectorRequestedException extends RuntimeException {
    public AlreadyDirectorRequestedException(String message) {
        super(message);
    }
}
