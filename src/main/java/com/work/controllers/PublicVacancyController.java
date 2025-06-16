package com.work.controllers;

import com.work.models.Vacancy;
import com.work.services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vacancies")
public class PublicVacancyController {

    private final VacancyService vacancyService;

    @Autowired
    public PublicVacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    // R4: Listagem de todas as vagas (em aberto) em uma única página
    // R4: Funcionalidade de filtrar as vagas (em aberto) por cidade
    @GetMapping
    public String listActiveVacancies(@RequestParam(value = "city", required = false) String city, Model model) {
        List<Vacancy> vacancies;
        if (city != null && !city.trim().isEmpty()) {
            vacancies = vacancyService.findActiveVacanciesByCity(city);
            model.addAttribute("selectedCity", city);
        } else {
            vacancies = vacancyService.findAllActiveVacancies();
        }

        // Get distinct cities for the filter dropdown
        List<String> cities = vacancyService.findAllActiveVacancies().stream()
                                            .map(Vacancy::getCity)
                                            .distinct()
                                            .sorted()
                                            .collect(Collectors.toList());

        model.addAttribute("vacancies", vacancies);
        model.addAttribute("cities", cities);
        return "vacancy/public-list"; // Path to public vacancy list page
    }

    // R5: Ao clicar em uma vaga (requisito R4), o profissional pode se candidatar à vaga.
    // This endpoint shows vacancy details. Candidacy itself is handled by ProfessionalController.
    @GetMapping("/{id}")
    public String viewVacancyDetails(@PathVariable("id") Long id, Model model) {
        Vacancy vacancy = vacancyService.findVacancyById(id);
        model.addAttribute("vacancy", vacancy);
        // For R5, this page should have a "Candidatar-se" button if user is logged in as Professional
        // and vacancy is still open.
        return "vacancy/details"; // Path to vacancy details page
    }
}
