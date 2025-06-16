package com.work.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String from = environment.getProperty("spring.mail.username");
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (MailException e) {
            logger.error("Error sending email to {}: {}", to, e.getMessage());
            // TODO: handle it (e.g., queue for later retry)
        }
    }

    @Override
    public void sendApplicationStatusUpdate(String to, String professionalName, String vacancyDescription, String status) {
        String subject = "Atualização do Status da sua Candidatura";
        String text = String.format(
            "Prezado(a) %s,\n\n" +
            "O status da sua candidatura para a vaga '%s' foi atualizado para: %s.\n\n" +
            "Atenciosamente,\n" +
            "Sistema de Vagas",
            professionalName, vacancyDescription, status
        );
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendInterviewInvitation(String to, String professionalName, String vacancyDescription, String interviewDetailsLink) {
        String subject = "Convite para Entrevista";
        String text = String.format(
            "Prezado(a) %s,\n\n" +
            "Parabéns! Você foi selecionado(a) para uma entrevista para a vaga '%s'.\n\n" +
            "Detalhes da entrevista e link para videoconferência: %s\n\n" +
            "Por favor, prepare-se para discutir suas qualificações e experiências.\n\n" +
            "Atenciosamente,\n" +
            "Sistema de Vagas",
            professionalName, vacancyDescription, interviewDetailsLink
        );
        sendSimpleMessage(to, subject, text);
    }
}
