package com.clinic.pet.petclinic.controller.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class CustomerRequestDto{    //TODO: co dodajemy do bazy najpierw customer czy zwierzatko, robie narazie ze klienta

    @NotNull
    private final String name;
    @NotNull
    private final String surname;

}
