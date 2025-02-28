package org.example.gobookingcommon.customException;

public class CompanyNoCreateException extends RuntimeException {
    public CompanyNoCreateException(String message) {
        super(message);
    }
}
