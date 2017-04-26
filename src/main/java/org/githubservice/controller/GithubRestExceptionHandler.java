package org.githubservice.controller;

import org.githubservice.service.GithubRepositoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GithubRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = GithubRepositoryException.class)
    public ResponseEntity<GithubServiceError> handleServiceException(
            GithubRepositoryException githubRepositoryException) {
        GithubServiceError error = new GithubServiceError();
        error.setStatusCode(githubRepositoryException.getStatusCode());
        if(githubRepositoryException.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
            error.setMessage("GitHub repository or user not found");
            return new ResponseEntity<GithubServiceError>(error, HttpStatus.NOT_FOUND);
        }else{
            // Not to confuse users.
            error.setMessage(githubRepositoryException.getLocalizedMessage());
            return new ResponseEntity<GithubServiceError>(error, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
