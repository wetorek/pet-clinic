package com.clinic.pet.petclinic.controller.thyme;

import com.clinic.pet.petclinic.service.VetService;
import com.clinic.pet.petclinic.service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/v1/vets")
@AllArgsConstructor
public class ThymeVetController {

    private final VetService vetService;

    @GetMapping
    public String getAllVisits(Model model) {
        model.addAttribute("vets", vetService.getAllVets());
        return "vets";
    }
}
