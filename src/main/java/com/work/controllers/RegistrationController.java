package com.work.controllers;

import com.work.models.Company;
import com.work.models.Professional;
import com.work.services.CompanyService;
import com.work.services.ProfessionalService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final ProfessionalService professionalService;
    private final CompanyService companyService;

    @Autowired
    public RegistrationController(ProfessionalService professionalService, CompanyService companyService) {
        this.professionalService = professionalService;
        this.companyService = companyService;
    }

    // == Professional Registration ==
    @GetMapping("/professional")
    public String showProfessionalRegistrationForm(Model model) {
        if (!model.containsAttribute("professional")) { // Keep professional if redirected from failed post
            model.addAttribute("professional", new Professional());
        }
        return "register/professional-register"; // Path to professional registration form
    }

    @PostMapping("/professional")
    public String registerProfessional(@Valid @ModelAttribute("professional") Professional professional,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.professional", bindingResult);
            redirectAttributes.addFlashAttribute("professional", professional);
            return "redirect:/register/professional";
        }
        try {
            professionalService.register(professional);
            redirectAttributes.addFlashAttribute("successMessage", "Cadastro de profissional realizado com sucesso! Faça o login.");
            return "redirect:/professional-login";
        } catch (Exception e) { // Catch more specific exceptions like DuplicateResourceException
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar profissional: " + e.getMessage());
            redirectAttributes.addFlashAttribute("professional", professional);
            return "redirect:/register/professional";
        }
    }

    // == Company Registration ==
    @GetMapping("/company")
    public String showCompanyRegistrationForm(Model model) {
         if (!model.containsAttribute("company")) {
            model.addAttribute("company", new Company());
        }
        return "register/company-register"; // Path to company registration form
    }

    @PostMapping("/company")
    public String registerCompany(@Valid @ModelAttribute("company") Company company,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.company", bindingResult);
            redirectAttributes.addFlashAttribute("company", company);
            return "redirect:/register/company";
        }
        try {
            companyService.register(company);
            redirectAttributes.addFlashAttribute("successMessage", "Cadastro de empresa realizado com sucesso! Faça o login.");
            return "redirect:/company-login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar empresa: " + e.getMessage());
            redirectAttributes.addFlashAttribute("company", company);
            return "redirect:/register/company";
        }
    }
}
