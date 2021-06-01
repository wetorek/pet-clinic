package com.clinic.pet.petclinic.repository;

import com.clinic.pet.petclinic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);

    boolean existsUserByUsername(String username);
}
