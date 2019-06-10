package com.example.demo.Exceptions;

public class CreatorNotFoundException extends RuntimeException
{
    public CreatorNotFoundException(Integer id)
    {
        super("Could not find creator with ID: " + id);
    }
}
