package com.work.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Locale;

public class HomeControllerTests {

    @Mock
    private MessageSource messageSource;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Default locale for tests, can be changed per test case if needed
        Locale.setDefault(new Locale("pt", "BR"));
    }

    @Test
    void testAdminLoginPageWithError() {
        String errorMessageKey = "login.error.invalidCredentials";
        String expectedMessage = "Email ou senha inválidos.";
        when(messageSource.getMessage(eq(errorMessageKey), isNull(), any(Locale.class))).thenReturn(expectedMessage);

        String viewName = homeController.adminLoginPage("true", null, model);

        assertEquals("login/admin-login", viewName);
        verify(model).addAttribute("errorMessage", expectedMessage);
        verify(messageSource).getMessage(eq(errorMessageKey), isNull(), any(Locale.class));
    }

    @Test
    void testAdminLoginPageWithLogout() {
        String logoutMessageKey = "login.logout.success";
        String expectedMessage = "Você foi desconectado com sucesso.";
        when(messageSource.getMessage(eq(logoutMessageKey), isNull(), any(Locale.class))).thenReturn(expectedMessage);

        String viewName = homeController.adminLoginPage(null, "true", model);

        assertEquals("login/admin-login", viewName);
        verify(model).addAttribute("logoutMessage", expectedMessage);
        verify(messageSource).getMessage(eq(logoutMessageKey), isNull(), any(Locale.class));
    }
}
