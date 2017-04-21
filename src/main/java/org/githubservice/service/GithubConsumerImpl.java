package org.githubservice.service;

import org.githubservice.exception.GithubRepositoryException;
import org.githubservice.model.GithubRepositoryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubConsumerImpl implements GithubConsumer {

    private Logger log = LoggerFactory.getLogger(GithubConsumerImpl.class);

    private static final String GITHUB_API_ROOT = "https://api.github.com/repos/";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GithubRepositoryModel getGithubRepositoryModelOnOwnerRepositoryName (
            String repositoryOwner, String repositoryName) throws GithubRepositoryException{

        String githubApiUrl = githubApiConstructor(repositoryOwner, repositoryName);
        try {
            ResponseEntity<GithubRepositoryModel> response
                    = restTemplate.getForEntity(githubApiUrl, GithubRepositoryModel.class);
            return response.getBody();
        } catch (HttpStatusCodeException httpException)  {
            String errorMessage = httpException.getLocalizedMessage();
            log.error("GitHub API response: "+ errorMessage);
            throw new GithubRepositoryException(errorMessage,
                    httpException.getStatusCode().value());
        }
    }

    private String githubApiConstructor(String repositoryOwner, String repositoryName) {
        return GITHUB_API_ROOT + repositoryOwner + "/" + repositoryName;
    }
}