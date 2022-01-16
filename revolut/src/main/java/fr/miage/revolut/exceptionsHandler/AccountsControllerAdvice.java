package fr.miage.revolut.exceptionsHandler;

import fr.miage.revolut.controllers.AccountsController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

@ResponseBody
@ControllerAdvice(basePackageClasses = AccountsController.class)
public class AccountsControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request,HttpServletRequest httpRequest) {
        Map<String, String> json = new LinkedHashMap<>();
        json.put("status",String.valueOf(HttpStatus.BAD_REQUEST.value()));
        ex.getBindingResult().getAllErrors().forEach( error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            json.put(fieldName, errorMessage);
        });
        json.put("path",httpRequest.getRequestURI());




        return json;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, String> dataIntegrityException(HttpServletRequest req, DataIntegrityViolationException e) {
        Map<String, String> json = new LinkedHashMap<>();
        json.put("status",String.valueOf(HttpStatus.CONFLICT.value()));
        json.put("path",req.getRequestURI());
        json.put("message", e.getLocalizedMessage());
        return json;
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> constraintViolationException(HttpServletRequest req, ConstraintViolationException e) {
        Map<String, String> json = new LinkedHashMap<>();
        json.put("status",String.valueOf(HttpStatus.BAD_REQUEST.value()));
        json.put("path",req.getRequestURI());
        json.put("message", e.getLocalizedMessage());
        return json;
    }





}
