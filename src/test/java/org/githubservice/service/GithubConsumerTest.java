package org.githubservice.service;

import org.githubservice.exception.GithubRepositoryException;
import org.githubservice.model.GithubRepositoryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class GithubConsumerTest {

    @MockBean
    private GithubConsumer githubConsumer;

    private String repositoryOwner = "jquery";
    private String repositoryName = "jquery";

    @Test
    public void whenExistingUserAndRepositoryNameProvidedThenRetrieveRepositoryDetails() {

        GithubRepositoryModel fakeRepositoryDetails = new GithubRepositoryModel();
        fakeRepositoryDetails.setFullName("jquery/jquery");
        fakeRepositoryDetails.setDescription("jQuery JavaScript Library");
        fakeRepositoryDetails.setCloneUrl("https://github.com/jquery/jquery.git");
        fakeRepositoryDetails.setStars(44470);
        fakeRepositoryDetails.setCreatedAt("2009-04-03T15:20:14Z");

        when(this.githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(repositoryOwner, repositoryName))
                .thenReturn(fakeRepositoryDetails);

        GithubRepositoryModel model = this.githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                repositoryOwner, repositoryName);

        assertEquals("jquery/jquery", model.getFullName());
        assertEquals("jQuery JavaScript Library", model.getDescription());
        assertEquals("https://github.com/jquery/jquery.git", model.getCloneUrl());
        assertEquals(44470, model.getStars());
        assertEquals("2009-04-03T15:20:14Z", model.getCreatedAt());
    }

    @Test (expected = GithubRepositoryException.class)
    public void whenNonExistingRepositoryNameProvidedThenThrowsException() {

        repositoryName = "jquery1234";

        when(this.githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(repositoryOwner, repositoryName))
                .thenThrow(GithubRepositoryException.class);

        githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                repositoryOwner, repositoryName);
    }

    @Test (expected = GithubRepositoryException.class)
    public void whenNonExistingUserProvidedThenThrowsException() {

        repositoryOwner = "jqueryUser";

        when(this.githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(repositoryOwner, repositoryName))
                .thenThrow(GithubRepositoryException.class);

        githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                repositoryOwner, repositoryName);
    }

}
