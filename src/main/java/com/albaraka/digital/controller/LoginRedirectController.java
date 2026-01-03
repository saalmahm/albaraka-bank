package com.albaraka.digital.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class LoginRedirectController {

    @GetMapping("/post-login")
    public String postLogin(Authentication authentication) {

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/admin/home";
        } else if (roles.contains("ROLE_AGENT_BANCAIRE")) {
            return "redirect:/agent/home";
        } else if (roles.contains("ROLE_CLIENT")) {
            return "redirect:/client/home";
        }

        return "redirect:/access-denied";
    }
}