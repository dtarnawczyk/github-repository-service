package org.githubservice.controller;

import org.githubservice.model.GithubRepositoryModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repositories/{owner}/{repositoryname}")
public class GithubRestController {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<GithubRepositoryModel> getGithubRepositoryDetails(
            @PathVariable String owner,
            @PathVariable String repositoryName) {

        GithubRepositoryModel githubRepoModel = new GithubRepositoryModel();


        return new ResponseEntity<GithubRepositoryModel>(githubRepoModel, HttpStatus.OK);
    }

}
