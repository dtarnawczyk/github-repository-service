package org.githubservice.service;

import org.githubservice.model.GithubRepositoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubConsumerImpl implements GithubConsumer {

    private static final String GITHUB_API_ROOT = "https://api.github.com/repos/";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GithubRepositoryModel getGithubRepositoryModelOnOwnerRepositoryName(
            String repositoryOwner, String repositoryName) {

        String githubApiUrl = githubApiConstructor(repositoryOwner, repositoryName);
        ResponseEntity<GithubRepositoryModel> response =
                restTemplate.getForEntity(githubApiUrl, GithubRepositoryModel.class);
        return response.getBody();
    }

    private String githubApiConstructor(String repositoryOwner, String repositoryName) {
        return GITHUB_API_ROOT + repositoryOwner + "/" + repositoryName;
    }


}
