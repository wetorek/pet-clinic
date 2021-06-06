package com.clinic.pet.petclinic.auth;

import org.apache.tomcat.util.codec.binary.Base64;
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
        return super.encode(pepperPassword(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        return super.matches(pepperPassword(rawPassword), encodedPassword);
    }

    private CharSequence pepperPassword(CharSequence rawPassword) {
        return new String(Base64.encodeBase64((rawPassword + pepper).getBytes()));
    }
}
