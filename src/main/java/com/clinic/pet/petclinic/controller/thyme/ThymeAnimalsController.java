package com.clinic.pet.petclinic.controller.thyme;

import com.clinic.pet.petclinic.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/animals")
@AllArgsConstructor
public class ThymeAnimalsController {
    private final AnimalService animalService;

    @GetMapping
    public String getAllAnimals(Model model) {
        model.addAttribute("animals", animalService.getAllAnimals());
        return "animals";
    }

}
