package com.clinic.pet.petclinic.auth;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.security.SecureRandom;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
@Configuration
@AllArgsConstructor
class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;
    private final IllegalAccessDeniedHandler illegalAccessDeniedHandler;

    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/authenticate/**")
                .permitAll()
                .anyRequest()
                .authenticated();
        http
                .addFilterBefore(new AuthenticationFilter(userDetailsService(), tokenService), AnonymousAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler(illegalAccessDeniedHandler);
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new PepperBCryptPasswordEncoder(10, new SecureRandom());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
