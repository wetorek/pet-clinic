package com.clinic.pet.petclinic;

import com.clinic.pet.petclinic.entity.Surgery;
import com.clinic.pet.petclinic.entity.Vet;
import com.clinic.pet.petclinic.repository.SurgeryRepository;
import com.clinic.pet.petclinic.repository.VetRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
@AllArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {
    private final VetRepository vetRepository;
    private final SurgeryRepository surgeryRepository;

    @Override
    public void run(String... args) {
        if (!vetRepository.existsById(1)) {
            var vet = new Vet(0, "John", "Doe", "10:00-18:00", null, new LinkedList<>());
            vetRepository.save(vet);
        }
        if (!vetRepository.existsById(2)) {
            var vet = new Vet(0, "Alex", "Smith", "09:00-17:00", null, new LinkedList<>());
            vetRepository.save(vet);
        }
        if (!surgeryRepository.existsById(1)){
            var surgery = new Surgery(0, "surgery1");
            surgeryRepository.save(surgery);
        }
        if (!surgeryRepository.existsById(2)){
            var surgery = new Surgery(0, "surgery2");
            surgeryRepository.save(surgery);
        }
    }
}
