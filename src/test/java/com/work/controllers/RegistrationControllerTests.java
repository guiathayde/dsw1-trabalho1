package com.work.controllers;

import com.work.models.Professional;
import com.work.services.ProfessionalService;
import com.work.services.CompanyService; // Assuming CompanyService is also a dependency
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Locale;

public class RegistrationControllerTests {

    @Mock
    private ProfessionalService professionalService;
    @Mock
    private CompanyService companyService; // Mock CompanyService
    @Mock
    private MessageSource messageSource;
    @Mock
    private BindingResult bindingResult;

    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        redirectAttributes = new RedirectAttributesModelMap();
        Locale.setDefault(new Locale("pt", "BR"));
    }

    @Test
    void testRegisterProfessionalSuccess() throws Exception {
        Professional professional = new Professional(); // Populate if needed
        String successMessageKey = "register.professional.success";
        String expectedMessage = "Cadastro de profissional realizado com sucesso!";

        when(bindingResult.hasErrors()).thenReturn(false);
        when(messageSource.getMessage(eq(successMessageKey), isNull(), any(Locale.class))).thenReturn(expectedMessage);
        // Mock professionalService.register to not throw an exception
            when(professionalService.register(any(Professional.class))).thenReturn(professional);


        String viewName = registrationController.registerProfessional(professional, bindingResult, redirectAttributes);

        assertEquals("redirect:/professional-login", viewName);
        assertEquals(expectedMessage, redirectAttributes.getFlashAttributes().get("successMessage"));
        verify(professionalService).register(professional);
        verify(messageSource).getMessage(eq(successMessageKey), isNull(), any(Locale.class));
    }

    @Test
    void testRegisterProfessionalBindingErrors() {
        Professional professional = new Professional();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = registrationController.registerProfessional(professional, bindingResult, redirectAttributes);

        assertEquals("redirect:/register/professional", viewName);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("org.springframework.validation.BindingResult.professional"));
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("professional"));
        verifyNoInteractions(professionalService); // Ensure service method not called
        verifyNoInteractions(messageSource); // No success/error messages from service interaction
    }

    @Test
    void testRegisterProfessionalServiceException() throws Exception {
        Professional professional = new Professional();
        String errorMessageKey = "register.professional.error";
        String exceptionMessage = "Email already exists";
        String expectedMessage = "Erro ao cadastrar profissional: " + exceptionMessage;

        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException(exceptionMessage)).when(professionalService).register(any(Professional.class));
        when(messageSource.getMessage(eq(errorMessageKey), any(Object[].class), any(Locale.class))).thenReturn(expectedMessage);

        String viewName = registrationController.registerProfessional(professional, bindingResult, redirectAttributes);

        assertEquals("redirect:/register/professional", viewName);
        assertEquals(expectedMessage, redirectAttributes.getFlashAttributes().get("errorMessage"));
        verify(professionalService).register(professional);
        verify(messageSource).getMessage(eq(errorMessageKey), any(Object[].class), any(Locale.class));
    }
}
