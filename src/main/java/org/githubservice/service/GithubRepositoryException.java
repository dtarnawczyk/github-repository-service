package org.githubservice.service;

public class GithubRepositoryException extends RuntimeException {

    private int statusCode = 0;
    private String message = "";

    public GithubRepositoryException(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
