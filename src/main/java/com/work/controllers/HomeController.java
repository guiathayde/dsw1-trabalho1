package com.work.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/vacancies"; // Redirect to public vacancy listing
    }

    @GetMapping("/admin-login")
    public String adminLoginPage(@RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "logout", required = false) String logout, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email ou senha inválidos.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Você foi desconectado com sucesso.");
        }
        return "login/admin-login"; // Path to admin login page
    }

    @GetMapping("/company-login")
    public String companyLoginPage(@RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "logout", required = false) String logout, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email ou senha inválidos.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Você foi desconectado com sucesso.");
        }
        return "login/company-login"; // Path to company login page
    }

    @GetMapping("/professional-login")
    public String professionalLoginPage(@RequestParam(value = "error", required = false) String error,
                                        @RequestParam(value = "logout", required = false) String logout, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email ou senha inválidos.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Você foi desconectado com sucesso.");
        }
        return "login/professional-login"; // Path to professional login page
    }

    // A general access denied page could be useful too.
    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "error/403"; // Or a more specific access-denied page
    }
}
