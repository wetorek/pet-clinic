package com.clinic.pet.petclinic.auth;

import com.clinic.pet.petclinic.exceptions.LogoutUserException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.clinic.pet.petclinic.auth.Constants.BEARER;

@AllArgsConstructor
public class TokenBlacklistFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(
                request.getHeader(HttpHeaders.AUTHORIZATION)
        ).filter(authHeader -> authHeader.startsWith(BEARER))
                .map(authHeader -> authHeader.substring(BEARER.length()))
                .ifPresent(token -> {
                    if (tokenService.isTokenBlacklisted(token)) {
                        throw new LogoutUserException("This user has logged out: {}" + token);
                    }
                });
        filterChain.doFilter(request, response);
    }
}
