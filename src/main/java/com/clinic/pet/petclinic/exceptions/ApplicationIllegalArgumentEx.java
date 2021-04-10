package com.clinic.pet.petclinic.exceptions;

public class ApplicationIllegalArgumentEx extends ApplicationException {
    public ApplicationIllegalArgumentEx() {
    }

    public ApplicationIllegalArgumentEx(String message) {
        super(message);
    }

    public ApplicationIllegalArgumentEx(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationIllegalArgumentEx(Throwable cause) {
        super(cause);
    }

    public ApplicationIllegalArgumentEx(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
