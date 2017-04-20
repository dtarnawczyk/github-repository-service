package org.githubservice.service;

import org.githubservice.model.GithubRepositoryModel;

public interface GithubConsumer {
    GithubRepositoryModel getGithubRepositoryModelOnOwnerRepositoryName(
            String setRepositoryOwner, String setRepositoryName);
}
