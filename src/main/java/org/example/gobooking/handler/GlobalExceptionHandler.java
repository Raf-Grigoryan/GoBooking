package org.example.gobooking.handler;


import org.example.gobooking.customException.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UserOnlyExistException.class)
    public ModelAndView message(UserOnlyExistException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessages", e.getMessage());
        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView message(UsernameNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessages", e.getMessage());
        modelAndView.setViewName("user/login");
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView message(MethodArgumentNotValidException e) {
        ModelAndView modelAndView = new ModelAndView();
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessages = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessages.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append(",");
        }
        modelAndView.addObject("errorMessages", errorMessages.toString());
        modelAndView.setViewName("user/register");
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

    @ExceptionHandler(AlreadyDirectorRequestedException.class)
    public ModelAndView handleCannotVerifyUserException(AlreadyDirectorRequestedException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("status", "409 Conflict");
        modelAndView.setViewName("error/globalErrorPage");
        return modelAndView;
    }


}
