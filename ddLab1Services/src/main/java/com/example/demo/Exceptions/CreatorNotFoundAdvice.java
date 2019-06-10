package com.example.demo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CreatorNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CreatorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String creatorNotFoundHandler(CreatorNotFoundException ex){
        return ex.getMessage();
    }
}
