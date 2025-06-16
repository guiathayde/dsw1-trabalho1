package com.work.services;

import com.work.models.Application;
import com.work.models.Professional;
import com.work.models.Vacancy;
import com.work.models.Company;
import com.work.repositories.ApplicationRepository;
import com.work.repositories.VacancyRepository; // To check deadline
import com.work.exceptions.ResourceNotFoundException;
import com.work.exceptions.DuplicateResourceException;
import com.work.exceptions.UnauthorizedOperationException; // Re-use or new one for this context
import com.work.exceptions.InvalidStateException; // New Exception for state change logic

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.work.services.EmailService;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Objects;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository; // To check deadline
    private final EmailService emailService; // Will be autowired later
    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);


    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository, VacancyRepository vacancyRepository , EmailService emailService ) {
        this.applicationRepository = applicationRepository;
        this.vacancyRepository = vacancyRepository;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public Application applyForVacancy(Professional professional, Vacancy vacancy, String resumePath) {
        // R5: Restrict one application per professional per vacancy
        if (applicationRepository.findByProfessionalAndVacancy(professional, vacancy).isPresent()) {
            throw new DuplicateResourceException("Professional has already applied for this vacancy.");
        }

        // R3: Check application deadline
        if (vacancy.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new InvalidStateException("Application deadline for this vacancy has passed.");
        }

        Application application = new Application();
        application.setProfessional(professional);
        application.setVacancy(vacancy);
        application.setResumePath(resumePath); // Assuming path is already determined by controller/file upload service
        application.setStatus("ABERTO"); // As per R7

        return applicationRepository.save(application);
    }

    @Override
    public List<Application> findApplicationsByProfessional(Professional professional) { // R7
        return applicationRepository.findByProfessional(professional);
    }

    @Override
    public Application findApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
    }

    @Override
    public List<Application> findApplicationsByVacancy(Vacancy vacancy) { // For R8
        return applicationRepository.findByVacancy(vacancy);
    }

    @Transactional
    @Override
    public Application updateApplicationStatus(Long applicationId, String status, Company company, String interviewDetails) { // R8
        Application application = findApplicationById(applicationId);
        Vacancy vacancy = application.getVacancy();

        // Ensure the company updating the status is the one that owns the vacancy
        if (!Objects.equals(vacancy.getCompany().getId(), company.getId())) {
            throw new UnauthorizedOperationException("Company is not authorized to update this application's status.");
        }

        // R8: Analysis phase starts after application deadline
        if (vacancy.getApplicationDeadline().isAfter(LocalDate.now()) && !application.getStatus().equals("ABERTO")) {
             // Allow pre-screening before deadline if desired, but typically status update is post-deadline
             // Or throw new InvalidStateException("Application period is still open. Status cannot be updated yet.");
        }

        if (!status.equals("NAO_SELECIONADO") && !status.equals("ENTREVISTA")) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Must be NAO_SELECIONADO or ENTREVISTA.");
        }

        application.setStatus(status);
        Application updatedApplication = applicationRepository.save(application);

            // Send email notification (R8) - Uncommented and active
            String professionalEmail = application.getProfessional().getEmail();
            String professionalName = application.getProfessional().getName();
            // It's better to get a summary of the vacancy or its title if available
            String vacancyTitle = vacancy.getDescription().length() > 50 ? vacancy.getDescription().substring(0, 50) + "..." : vacancy.getDescription();


            if (status.equals("NAO_SELECIONADO")) {
                emailService.sendApplicationStatusUpdate(professionalEmail, professionalName, vacancyTitle, "Não Selecionado(a)");
            } else if (status.equals("ENTREVISTA")) {
                if (interviewDetails == null || interviewDetails.trim().isEmpty()) {
                    // Log a warning or throw an exception if interview details are mandatory for "ENTREVISTA" status
                    logger.warn("Interview details are missing for an application marked for INTERVIEW. Application ID: {}", applicationId);
                    // Potentially: throw new InvalidStateException("Interview details are required for status ENTREVISTA.");
                }
                emailService.sendInterviewInvitation(professionalEmail, professionalName, vacancyTitle, interviewDetails);
            }

        return updatedApplication;
    }
}
