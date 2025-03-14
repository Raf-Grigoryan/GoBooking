package org.example.gobookingweb.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.gobookingcommon.customException.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UserOnlyExistException.class)
    public ModelAndView message(UserOnlyExistException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessages", e.getMessage());
        modelAndView.setViewName("auth/register");
        return modelAndView;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView message(UsernameNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.setViewName("auth/login");
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        BindingResult bindingResult = e.getBindingResult();

        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        String requestURI = request.getRequestURI();

        if (requestURI.contains("/auth/register")) {
            modelAndView.setViewName("auth/register");
        } else if (requestURI.contains("/auth/create-card")) {
            modelAndView.setViewName("error/globalErrorPage");
            modelAndView.addObject("status", "400 bad request");
        }

        modelAndView.addObject("errorMessages", errorMessages);
        return modelAndView;
    }

    @ExceptionHandler(CannotVerifyUserException.class)
    public ModelAndView handleCannotVerifyUserException(CannotVerifyUserException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "400 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView message(UserNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "400 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(CardOnlyExistException.class)
    public ModelAndView message(CardOnlyExistException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(AddressOnlyExistException.class)
    public ModelAndView message(AddressOnlyExistException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(CompanyAlreadyExistsException.class)
    public ModelAndView message(CompanyAlreadyExistsException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(AlreadyDirectorRequestedException.class)
    public ModelAndView handleCannotVerifyUserException(AlreadyDirectorRequestedException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 Conflict");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleInvalidImageFormat(IllegalArgumentException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "400 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "500 internal server error");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(AlreadyRoleChangeRequestException.class)
    public ModelAndView message(AlreadyRoleChangeRequestException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(CompanyNoCreateException.class)
    public ModelAndView message(CompanyNoCreateException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(CardCountException.class)
    public ModelAndView message(CardCountException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(SubscriptionOnlyExistException.class)
    public ModelAndView message(SubscriptionOnlyExistException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView message(EntityNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "404 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }


    @ExceptionHandler(InsufficientFundsException.class)
    public ModelAndView handleCannotVerifyUserException(InsufficientFundsException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 Conflict");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(DirectorNotMatchException.class)
    public ModelAndView message(DirectorNotMatchException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(SubscriptionNotValidException.class)
    public ModelAndView message(SubscriptionNotValidException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 Conflict");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }

    @ExceptionHandler(CardNotExistException.class)
    public ModelAndView message(CardNotExistException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 bad request");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }
}
