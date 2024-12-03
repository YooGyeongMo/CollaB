package com.example.springsecurityexample.match;

public class Error {
    private boolean isSuccess;
    private String message;
    public Error(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
