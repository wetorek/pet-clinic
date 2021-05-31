package com.clinic.pet.petclinic.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    VET,
    ADMIN,
    CLIENT;

    @Override
    public String getAuthority() {
        return toString();
    }
}
