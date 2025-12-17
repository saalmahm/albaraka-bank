package com.albaraka.digital.security.jwt;

import com.albaraka.digital.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // Pour manipuler le JWT (génération, extraction, validation)
    private final CustomUserDetailsService userDetailsService; // Pour récupérer l'utilisateur depuis la base

    /**
     * Méthode exécutée pour chaque requête HTTP
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Récupère le header Authorization
        // "Authorization: Bearer <token>"
        String authHeader = request.getHeader("Authorization");

        // Si pas de header ou ne commence pas par "Bearer ", passer la requête au filtre suivant
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Récupération du token (on enlève "Bearer ")
        String token = authHeader.substring(7);

        // Extraction du username (email) depuis le token
        String username = jwtService.extractUsername(token);

        // Vérifie si l'utilisateur n'est pas déjà authentifié dans le contexte Spring
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Récupère l'utilisateur depuis la DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Vérifie si le token est valide (correspond à l'utilisateur et non expiré)
            if (jwtService.isTokenValid(token, userDetails)) {

                // Crée un objet Authentication pour Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities() // Roles/Authorities
                        );

                // Ajoute les détails de la requête (IP, session, etc.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Place l'utilisateur authentifié dans le contexte Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Passe la requête au prochain filtre dans la chaîne
        filterChain.doFilter(request, response);
    }
}
