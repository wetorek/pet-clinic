package com.clinic.pet.petclinic.exceptions;

public class LogoutUserException extends ApplicationException {
    public LogoutUserException() {
    }

    public LogoutUserException(String message) {
        super(message);
    }

    public LogoutUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogoutUserException(Throwable cause) {
        super(cause);
    }

    public LogoutUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
