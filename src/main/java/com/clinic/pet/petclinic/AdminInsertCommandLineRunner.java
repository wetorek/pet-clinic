package com.clinic.pet.petclinic;

import com.clinic.pet.petclinic.entity.*;
import com.clinic.pet.petclinic.repository.CustomerRepository;
import com.clinic.pet.petclinic.repository.SurgeryRepository;
import com.clinic.pet.petclinic.repository.UserRepository;
import com.clinic.pet.petclinic.repository.VetRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@AllArgsConstructor
@Profile("!test")
public class AdminInsertCommandLineRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final VetRepository vetRepository;
    private final PasswordEncoder passwordEncoder;
    private final SurgeryRepository surgeryRepository;

    @Override
    public void run(String... args) {
        var admin = new User("admin", passwordEncoder.encode("admin"), Role.ROLE_ADMIN, AccountState.ACTIVE);
        var blockedUser = new User("admin2", passwordEncoder.encode("admin2"), Role.ROLE_ADMIN, AccountState.BLOCKED);
        var client = new Customer(-1, "client", passwordEncoder.encode("client"), Role.ROLE_CLIENT, AccountState.ACTIVE, "name", "surname");
        var vet = new Vet(-1, "vet", "vet name", "vet surname", LocalTime.of(9, 0), LocalTime.of(17, 0));
        vet.setPassword(passwordEncoder.encode("vet"));
        var surgery = new Surgery(-1, "Vet surgery");

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(admin);
        }
        if (!userRepository.existsByUsername("admin2")) {
            userRepository.save(blockedUser);
        }
        if (!userRepository.existsByUsername("client")) {
            customerRepository.save(client);
        }
        if (!userRepository.existsByUsername("vet")) {
            vetRepository.save(vet);
        }
        if (!surgeryRepository.existsById(1)) {
            surgeryRepository.save(surgery);
        }
    }
}
