package com.clinic.pet.petclinic.controller.rest;

import java.util.List;

public interface Mapper <T, U>{
    U mapToDto (T t);

    List<U> mapListToDto (List<T> list);
}
