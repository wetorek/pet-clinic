package com.clinic.pet.petclinic.exceptions;

public class IllegalVisitStateException extends ApplicationException {
    public IllegalVisitStateException() {
    }

    public IllegalVisitStateException(String message) {
        super(message);
    }

    public IllegalVisitStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalVisitStateException(Throwable cause) {
        super(cause);
    }

    public IllegalVisitStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
