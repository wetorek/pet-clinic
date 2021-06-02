package com.clinic.pet.petclinic.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_VET,
    ROLE_ADMIN,
    ROLE_CLIENT;

    @Override
    public String getAuthority() {
        return toString();
    }
}
