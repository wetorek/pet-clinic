package com.clinic.pet.petclinic;

import com.clinic.pet.petclinic.entity.Vet;
import com.clinic.pet.petclinic.repository.VetRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
@AllArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {
    private final VetRepository vetRepository;

    @Override
    public void run(String... args) {
        if (!vetRepository.existsById(1)) {
            var vet = new Vet(0, "John", "Doe", "Lorem ipsum", null, new LinkedList<>());
            vetRepository.save(vet);
        }
    }
}
