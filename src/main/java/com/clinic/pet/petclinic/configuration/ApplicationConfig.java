package com.clinic.pet.petclinic.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ApplicationConfig {
    @Bean
    Clock clock() {
        return Clock.system(ZoneId.of("Europe/Paris"));
    }
}
