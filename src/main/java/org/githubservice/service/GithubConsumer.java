package org.githubservice.service;

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
public class GithubConsumer {

    private static final Logger log = LoggerFactory.getLogger(GithubConsumer.class);

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.api.retriesCounter}")
    private int retriesCounter;

    @Value("${github.api.retriesTimeout}")
    private int retriesTimeout;

    @Autowired
    private RestTemplate restTemplate;

    public GithubRepositoryModel getRepositoryDetails (
            String repositoryOwner, String repositoryName) throws GithubRepositoryException {
        String githubApiUrl = githubApiConstructor(repositoryOwner, repositoryName);
        while(true) {
            log.debug("GitHub API call attempt");
            try {
                ResponseEntity<GithubRepositoryModel> response
                        = restTemplate.getForEntity(githubApiUrl, GithubRepositoryModel.class);
                return response.getBody();
            } catch (HttpStatusCodeException httpException)  {
                retriesCounter -= 1;
                if(retriesCounter <= 0) {
                    throw createGithubRepositoryException(httpException);
                }
                waitUntilNextRetry();
            }
        }
    }

    private String githubApiConstructor(String repositoryOwner, String repositoryName) {
        return String.join("/", githubApiUrl, repositoryOwner, repositoryName);
    }

    private GithubRepositoryException createGithubRepositoryException(
            HttpStatusCodeException httpException){
        String errorMessage = httpException.getLocalizedMessage();
        log.error("GitHub API exception caused by: ", httpException);
        return new GithubRepositoryException(errorMessage,
                httpException.getStatusCode().value());
    }

    private void waitUntilNextRetry() {
        try{
            Thread.sleep(this.retriesTimeout);
        }catch(InterruptedException interruptedException) {
            log.error("Retrying timeout interruption caused by: ",
                    interruptedException);
        }
    }
}
