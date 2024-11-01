package com.taskmanagement.taskmanagement.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException ex, Model model) {
        model.addAttribute("errorMessage", "The requested task was not found.");
        return "exception/error-page";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", "The entity you are trying to access does not exist.");
        return "exception/error-page";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", "Invalid input provided.");
        return "exception/error-page";
    }

}
