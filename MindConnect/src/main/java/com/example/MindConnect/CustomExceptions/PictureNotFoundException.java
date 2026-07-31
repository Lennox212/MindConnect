package com.example.MindConnect.CustomExceptions;

public class PictureNotFoundException extends RuntimeException {
    public PictureNotFoundException(String message) {
        super(message);
    }
}
