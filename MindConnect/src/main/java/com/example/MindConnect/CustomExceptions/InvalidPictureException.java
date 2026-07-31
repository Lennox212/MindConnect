package com.example.MindConnect.CustomExceptions;

public class InvalidPictureException extends RuntimeException {
    public InvalidPictureException(String message) {
        super(message);
    }
}
