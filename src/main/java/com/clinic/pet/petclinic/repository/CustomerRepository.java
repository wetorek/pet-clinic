package com.clinic.pet.petclinic.repository;

import com.clinic.pet.petclinic.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {


}
