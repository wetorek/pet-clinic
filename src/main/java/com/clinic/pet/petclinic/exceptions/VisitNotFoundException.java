package com.clinic.pet.petclinic.exceptions;

public class VisitNotFoundException extends ResourceNotFoundException {
    public VisitNotFoundException() {
    }

    public VisitNotFoundException(String message) {
        super(message);
    }

    public VisitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VisitNotFoundException(Throwable cause) {
        super(cause);
    }

    public VisitNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
