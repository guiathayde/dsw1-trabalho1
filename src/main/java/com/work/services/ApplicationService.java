package com.work.services;

import com.work.models.Application;
import com.work.models.Professional;
import com.work.models.Vacancy;
import com.work.models.Company; // Needed for R8
import java.util.List;

public interface ApplicationService {
    Application applyForVacancy(Professional professional, Vacancy vacancy, String resumePath); // R5
    List<Application> findApplicationsByProfessional(Professional professional); // R7
    Application findApplicationById(Long applicationId);
    List<Application> findApplicationsByVacancy(Vacancy vacancy); // For company to view applicants (R8)
    Application updateApplicationStatus(Long applicationId, String status, Company company, String interviewDetails); // R8 (interviewDetails for email)
    // getResume(Long applicationId); // For downloading resume
}
