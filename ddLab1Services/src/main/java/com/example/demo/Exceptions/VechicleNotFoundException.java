package com.example.demo.Exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;

public class VechicleNotFoundException extends RuntimeException {
    public VechicleNotFoundException(Integer id){
        super("Could not find movie with ID: " + id);
    }

}
