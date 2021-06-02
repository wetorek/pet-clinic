package com.clinic.pet.petclinic.auth;

import com.clinic.pet.petclinic.entity.AccountState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class AuthenticatedUser extends User {
    private int userId;
    private AccountState accountState;

    public AuthenticatedUser(int userId, String username, String password, AccountState accountState, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.accountState = accountState;
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked() && accountState != AccountState.BLOCKED;
    }
}
