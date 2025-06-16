package com.work.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
// Assuming your base package for components that might need scanning is com.work.work
// Adjust if your project structure is different, though for this specific config,
// component scanning of its own package might not be strictly necessary unless other
// config-related beans are in br.ufscar.dc.dsw.config.
// For now, let's use the reference project's style.
@ComponentScan(basePackages = "com.work.config")
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to /home, and define /home and /login views
        // These can be adjusted based on actual landing pages and login page names
        registry.addViewController("/").setViewName("redirect:/home");
        registry.addViewController("/home").setViewName("home"); // Assumes a 'home.html' template
        registry.addViewController("/login").setViewName("login"); // Assumes a 'login.html' template
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        // Set default locale to Portuguese Brazil
        slr.setDefaultLocale(new Locale("pt", "BR"));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // Parameter name to look for in request to change locale
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the locale change interceptor
        registry.addInterceptor(localeChangeInterceptor());
    }

    // Add addFormatters method here if custom formatters/converters are needed later
    // For example:
    // @Override
    // public void addFormatters(FormatterRegistry registry) {
    //     registry.addConverter(new YourCustomConverter());
    // }
}
