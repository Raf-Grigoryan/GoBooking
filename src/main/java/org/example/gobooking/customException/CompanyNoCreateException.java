package org.example.gobooking.customException;

public class CompanyNoCreateException extends RuntimeException {
    public CompanyNoCreateException(String message) {
        super(message);
    }
}
