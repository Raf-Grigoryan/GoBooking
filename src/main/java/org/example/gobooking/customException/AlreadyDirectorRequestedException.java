package org.example.gobooking.customException;

public class AlreadyDirectorRequestedException extends RuntimeException {
    public AlreadyDirectorRequestedException(String message) {
        super(message);
    }
}
