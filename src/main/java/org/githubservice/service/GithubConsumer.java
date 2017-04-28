package org.githubservice.service;

import org.githubservice.model.GithubRepositoryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubConsumer {

    private static final Logger log = LoggerFactory.getLogger(GithubConsumer.class);

    @Value("${github.api.url}")
    private String githubApiUrl;

    private final RestTemplate restTemplate;

    private final RetryTemplate retryTemplate;

    public GithubConsumer(RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    public GithubRepositoryModel getRepositoryDetails (
            String repositoryOwner, String repositoryName) throws GithubRepositoryException {
        String githubApiUrl = githubApiConstructor(repositoryOwner, repositoryName);
        try {
            return retryTemplate.execute(retryCallback -> {
                log.debug("GitHub API call attempt");
                ResponseEntity<GithubRepositoryModel> response =
                        this.restTemplate.getForEntity(githubApiUrl, GithubRepositoryModel.class);
                return response.getBody();
            });
        } catch (HttpStatusCodeException httpException) {
            throw createGithubRepositoryException(httpException);
        }
    }

    private String githubApiConstructor(String repositoryOwner, String repositoryName) {
        return String.join("/", githubApiUrl, repositoryOwner, repositoryName);
    }

    private GithubRepositoryException createGithubRepositoryException(
            HttpStatusCodeException httpException){
        String errorMessage = httpException.getLocalizedMessage();
        log.error("GitHub API exception caused by: ", httpException.getMostSpecificCause());
        return new GithubRepositoryException(errorMessage, httpException.getStatusCode().value());
    }

}
