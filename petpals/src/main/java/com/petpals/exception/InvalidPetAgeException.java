package com.petpals.exception;

public class InvalidPetAgeException extends Exception {
    public InvalidPetAgeException(String message) {
        super(message);
    }
}