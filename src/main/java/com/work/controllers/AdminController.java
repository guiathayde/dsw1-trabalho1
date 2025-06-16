package com.work.controllers;

import com.work.models.Professional;
import com.work.models.Company;
import com.work.services.ProfessionalService;
import com.work.services.CompanyService;
import com.work.exceptions.DuplicateResourceException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProfessionalService professionalService;
    private final CompanyService companyService;

    @Autowired
    public AdminController(ProfessionalService professionalService, CompanyService companyService) {
        this.professionalService = professionalService;
        this.companyService = companyService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Add any necessary data for the dashboard
        model.addAttribute("professionalCount", professionalService.findAll().size());
        model.addAttribute("companyCount", companyService.findAll().size());
        return "admin/dashboard"; // Path to admin dashboard page
    }

    // == Professional CRUD (R1) ==
    @GetMapping("/professionals")
    public String listProfessionals(Model model) {
        List<Professional> professionals = professionalService.findAll();
        model.addAttribute("professionals", professionals);
        return "admin/professionals/list"; // Path to professional list page
    }

    @GetMapping("/professionals/add")
    public String showAddProfessionalForm(Model model) {
        if (!model.containsAttribute("professional")) {
            model.addAttribute("professional", new Professional());
        }
        return "admin/professionals/form"; // Path to professional add/edit form
    }

    @PostMapping("/professionals/save")
    public String saveProfessional(@Valid @ModelAttribute("professional") Professional professional,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("professional", professional); // Keep professional object with errors
            // Ensure password is not sent back to the form unless it's an edit and we want to show it's set
            if (professional.getId() != null) professional.setPassword(""); // Clear password for edit form if validation fails
            return "admin/professionals/form";
        }
        try {
            if (professional.getId() == null) { // New Professional
                professionalService.register(professional); // Uses password encoding
                redirectAttributes.addFlashAttribute("successMessage", "Profissional cadastrado com sucesso!");
            } else { // Existing Professional
                // Ensure password is only updated if a new one is provided
                Professional existingProf = professionalService.findById(professional.getId());
                if (professional.getPassword() == null || professional.getPassword().isEmpty()) {
                    professional.setPassword(existingProf.getPassword()); // Keep old password if not changed
                } else {
                     // If password is provided, it will be encoded by the update method if it's different from placeholder
                }
                professionalService.update(professional.getId(), professional);
                redirectAttributes.addFlashAttribute("successMessage", "Profissional atualizado com sucesso!");
            }
            return "redirect:/admin/professionals";
        } catch (DuplicateResourceException e) {
            model.addAttribute("professional", professional);
            if (professional.getId() != null) professional.setPassword("");
            bindingResult.reject("globalError", e.getMessage()); // Add global error
            return "admin/professionals/form";
        } catch (Exception e) {
            model.addAttribute("professional", professional);
            if (professional.getId() != null) professional.setPassword("");
            bindingResult.reject("globalError", "Erro ao salvar profissional: " + e.getMessage());
            return "admin/professionals/form";
        }
    }

    @GetMapping("/professionals/edit/{id}")
    public String showEditProfessionalForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Professional professional = professionalService.findById(id);
            professional.setPassword(""); // Clear password before sending to form
            model.addAttribute("professional", professional);
            return "admin/professionals/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Profissional não encontrado.");
            return "redirect:/admin/professionals";
        }
    }

    @GetMapping("/professionals/delete/{id}")
    public String deleteProfessional(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            professionalService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Profissional excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir profissional: " + e.getMessage());
        }
        return "redirect:/admin/professionals";
    }

    // == Company CRUD (R2) ==
    @GetMapping("/companies")
    public String listCompanies(Model model) {
        List<Company> companies = companyService.findAll();
        model.addAttribute("companies", companies);
        return "admin/companies/list"; // Path to company list page
    }

    @GetMapping("/companies/add")
    public String showAddCompanyForm(Model model) {
         if (!model.containsAttribute("company")) {
            model.addAttribute("company", new Company());
        }
        return "admin/companies/form"; // Path to company add/edit form
    }

    @PostMapping("/companies/save")
    public String saveCompany(@Valid @ModelAttribute("company") Company company,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("company", company);
            if (company.getId() != null) company.setPassword("");
            return "admin/companies/form";
        }
        try {
            if (company.getId() == null) { // New Company
                companyService.register(company); // Uses password encoding
                redirectAttributes.addFlashAttribute("successMessage", "Empresa cadastrada com sucesso!");
            } else { // Existing Company
                Company existingCompany = companyService.findById(company.getId());
                if (company.getPassword() == null || company.getPassword().isEmpty()) {
                    company.setPassword(existingCompany.getPassword());
                }
                companyService.update(company.getId(), company);
                redirectAttributes.addFlashAttribute("successMessage", "Empresa atualizada com sucesso!");
            }
            return "redirect:/admin/companies";
        } catch (DuplicateResourceException e) {
            model.addAttribute("company", company);
            if (company.getId() != null) company.setPassword("");
            bindingResult.reject("globalError", e.getMessage());
            return "admin/companies/form";
        } catch (Exception e) {
            model.addAttribute("company", company);
            if (company.getId() != null) company.setPassword("");
            bindingResult.reject("globalError", "Erro ao salvar empresa: " + e.getMessage());
            return "admin/companies/form";
        }
    }

    @GetMapping("/companies/edit/{id}")
    public String showEditCompanyForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
         try {
            Company company = companyService.findById(id);
            company.setPassword(""); // Clear password
            model.addAttribute("company", company);
            return "admin/companies/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Empresa não encontrada.");
            return "redirect:/admin/companies";
        }
    }

    @GetMapping("/companies/delete/{id}")
    public String deleteCompany(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            companyService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Empresa excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir empresa: " + e.getMessage());
        }
        return "redirect:/admin/companies";
    }
}
