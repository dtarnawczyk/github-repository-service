package org.githubservice.service;

import org.githubservice.exception.GithubRepositoryException;
import org.githubservice.model.GithubRepositoryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubConsumerImpl implements GithubConsumer {

    private Logger log = LoggerFactory.getLogger(GithubConsumerImpl.class);

    @Value("${github.api.url}")
    private String githubApiUrl;

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
            throw createGithubRepositoryException(httpException);
        }
    }

    private String githubApiConstructor(String repositoryOwner, String repositoryName) {
        return String.join("/", githubApiUrl, repositoryOwner, repositoryName);
    }

    private GithubRepositoryException createGithubRepositoryException(
            HttpStatusCodeException httpException){
        String errorMessage = httpException.getLocalizedMessage();
        log.error("GitHub API response: "+ errorMessage);
        return new GithubRepositoryException(errorMessage,
                httpException.getStatusCode().value());
    }
}
