package org.githubservice.controller;

import org.githubservice.domain.GithubRepositoryModel;
import org.githubservice.service.GithubConsumer;
import org.githubservice.service.GithubRepositoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repositories/{owner}/{repositoryname}")
public class GithubRestController {

    private final GithubConsumer githubConsumer;

    public GithubRestController(GithubConsumer githubConsumer) {
        this.githubConsumer = githubConsumer;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<GithubRepositoryModel> getGithubRepositoryDetails(
            @PathVariable String owner,
            @PathVariable String repositoryname) throws GithubRepositoryException {
        return new ResponseEntity<GithubRepositoryModel>(
                githubConsumer.getRepositoryDetails(owner, repositoryname), HttpStatus.OK);
    }
}
