package com.clinic.pet.petclinic;

import com.clinic.pet.petclinic.entity.AccountState;
import com.clinic.pet.petclinic.entity.Role;
import com.clinic.pet.petclinic.entity.User;
import com.clinic.pet.petclinic.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Profile("!test")
public class AdminInsertCommandLineRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        var admin = new User("admin", passwordEncoder.encode("admin"), Role.ROLE_ADMIN, AccountState.ACTIVE);
        var blockedUser = new User("admin2", passwordEncoder.encode("admin2"), Role.ROLE_ADMIN, AccountState.BLOCKED);
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(admin);
        }
        if (!userRepository.existsByUsername("admin2")) {
            userRepository.save(blockedUser);
        }
    }
}
