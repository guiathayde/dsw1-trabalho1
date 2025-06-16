package com.work.services;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendApplicationStatusUpdate(String to, String professionalName, String vacancyDescription, String status);

    void sendInterviewInvitation(String to, String professionalName, String vacancyDescription, String interviewDetailsLink);

    // Potentially add more methods for other email types if needed
}
