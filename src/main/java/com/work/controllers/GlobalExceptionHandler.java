package com.work.controllers;

import com.work.exceptions.DuplicateResourceException;
import com.work.exceptions.InvalidStateException;
import com.work.exceptions.ResourceNotFoundException;
import com.work.exceptions.UnauthorizedOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(HttpServletRequest req, ResourceNotFoundException ex, Model model) {
        logger.error("ResourceNotFoundException: URL={}, Error={}", req.getRequestURL(), ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorStatus", HttpStatus.NOT_FOUND.value());
        model.addAttribute("url", req.getRequestURL());
        return new ModelAndView("error/404"); // Path to a generic 404 error page
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoHandlerFoundException(HttpServletRequest req, NoHandlerFoundException ex, Model model) {
        logger.error("NoHandlerFoundException: URL={}, Error={}", req.getRequestURL(), ex.getMessage());
        model.addAttribute("errorMessageKey", "error.page.notfound"); // Use key
        model.addAttribute("errorStatus", HttpStatus.NOT_FOUND.value());
        model.addAttribute("url", req.getRequestURL());
        return new ModelAndView("error/404");
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleDuplicateResourceException(HttpServletRequest req, DuplicateResourceException ex, Model model) {
        logger.error("DuplicateResourceException: URL={}, Error={}", req.getRequestURL(), ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorStatus", HttpStatus.CONFLICT.value());
        return new ModelAndView("error/error"); // Path to a generic error page
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleUnauthorizedOperationException(HttpServletRequest req, UnauthorizedOperationException ex, Model model) {
        logger.error("UnauthorizedOperationException: URL={}, Error={}", req.getRequestURL(), ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorStatus", HttpStatus.FORBIDDEN.value());
        return new ModelAndView("error/403"); // Path to a generic 403 error page
    }

    @ExceptionHandler(InvalidStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleInvalidStateException(HttpServletRequest req, InvalidStateException ex, Model model) {
        logger.error("InvalidStateException: URL={}, Error={}", req.getRequestURL(), ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorStatus", HttpStatus.BAD_REQUEST.value());
        return new ModelAndView("error/error");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleAllUncaughtException(HttpServletRequest req, Exception ex, Model model) {
        logger.error("UnhandledException: URL={}, Error={}", req.getRequestURL(), ex.getMessage(), ex);
        model.addAttribute("errorMessageKey", "error.server.unexpected"); // Use key
        model.addAttribute("errorStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        // For security, don't pass ex.getMessage() directly to the user for generic exceptions in production
        // model.addAttribute("detailedError", ex.getMessage()); // Maybe only if in debug mode
        return new ModelAndView("error/error");
    }
}
