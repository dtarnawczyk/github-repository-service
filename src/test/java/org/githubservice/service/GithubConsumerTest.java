package org.githubservice.service;

import org.githubservice.Application;
import org.githubservice.model.GithubRepositoryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@EnableConfigurationProperties
public class GithubConsumerTest {

    @Autowired
    private GithubConsumer githubConsumer;

    private String user = "jquery";
    private String repositoryName = "jquery";

    @Test
    public void whenExistingUserAndRepositoryNameProvidedThenRetrieveRepositoryDetails() {
        GithubRepositoryModel model = githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                user, repositoryName);
        assertEquals("jQuery JavaScript Library", model.getDescription());
        assertEquals("jquery/jquery", model.getFullName());
        assertEquals("https://github.com/jquery/jquery.git", model.getCloneUrl());
    }

}
