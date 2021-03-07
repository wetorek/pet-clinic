package com.clinic.pet.petclinic.controller.thyme;


import com.clinic.pet.petclinic.service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/visits")
@AllArgsConstructor
public class ThymeVisitController {
    private final VisitService visitService;

    @GetMapping
    public String getAllVisits(Model model) {

        //todo implement
        return "";
    }
}
