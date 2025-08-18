package com.example.staffaccounting.security.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Anatoliy Shikin
 */
@Controller
public class UserPageController {
    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("login", principal.getAttribute("login"));
            model.addAttribute("id", principal.getAttribute("id"));
            model.addAttribute("email", principal.getAttribute("email"));
        }
        return "user";
    }
}
