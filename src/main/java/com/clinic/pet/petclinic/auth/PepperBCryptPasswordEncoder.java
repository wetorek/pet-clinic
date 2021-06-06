package com.clinic.pet.petclinic.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public class PepperBCryptPasswordEncoder extends BCryptPasswordEncoder {
    @Value("{$app.security.pepper}")
    private String pepper;

    public PepperBCryptPasswordEncoder(int strength, SecureRandom random) {
        super(strength, random);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        return super.encode(rawPassword + pepper);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        return super.matches(rawPassword + pepper, encodedPassword);
    }
}
