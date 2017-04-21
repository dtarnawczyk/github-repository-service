package org.githubservice.controller;

import org.githubservice.exception.GithubRepositoryException;
import org.githubservice.exception.GithubServiceError;
import org.githubservice.model.GithubRepositoryModel;
import org.githubservice.service.GithubConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repositories/{owner}/{repositoryname}")
public class GithubRestController {

    @Autowired
    private GithubConsumer githubConsumer;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getGithubRepositoryDetails(
            @PathVariable String owner,
            @PathVariable String repositoryname) {

        try {

            GithubRepositoryModel githubRepoModel =
                    githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                            owner, repositoryname);
            return new ResponseEntity<GithubRepositoryModel>(githubRepoModel, HttpStatus.OK);

        } catch (GithubRepositoryException serviceException) {

            GithubServiceError error = new GithubServiceError();
            error.setStatusCode(serviceException.getStatusCode());
            if(serviceException.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                error.setMessage("GitHub repository or user not found");
                return new ResponseEntity<GithubServiceError>(error, HttpStatus.NOT_FOUND);
            }else{
                error.setMessage(serviceException.getLocalizedMessage());
                return new ResponseEntity<GithubServiceError>(error, HttpStatus.SERVICE_UNAVAILABLE);
            }
        }
    }
}
