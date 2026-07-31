package com.example.MindConnect.CustomExceptions;

public class PictureUploadException extends RuntimeException {
    public PictureUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
