package com.techshop.exception;

public class IncompleteOrderException extends Exception {
    public IncompleteOrderException(String message) {
        super(message);
    }
}