package org.githubservice.service;

import org.githubservice.exception.GithubRepositoryException;
import org.githubservice.model.GithubRepositoryModel;
import org.githubservice.util.DateUtil;
import org.githubservice.util.MockGithubRepositoryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

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
        Date expectedDate = DateUtil.createDateFromIsoString("2009-04-03T15:20:14Z");
        GithubRepositoryModel mockGithubRepositoryModel = new MockGithubRepositoryModel.Builder()
                .setFullName("jquery/jquery")
                .setDescription("jQuery JavaScript Library")
                .setCloneUrl("https://github.com/jquery/jquery.git")
                .setStars(44470)
                .setCreatedAt(expectedDate)
                .build();

        when(this.githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(repositoryOwner, repositoryName))
                .thenReturn(mockGithubRepositoryModel);

        GithubRepositoryModel model = this.githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                repositoryOwner, repositoryName);
        assertEquals("jquery/jquery", model.getFullName());
        assertEquals("jQuery JavaScript Library", model.getDescription());
        assertEquals("https://github.com/jquery/jquery.git", model.getCloneUrl());
        assertEquals(44470, model.getStars());
        assertEquals(expectedDate, model.getCreatedAt());
    }

    @SuppressWarnings("unchecked")
    @Test (expected = GithubRepositoryException.class)
    public void whenNonExistingRepositoryNameProvidedThenThrowsException() {
        String unavailableRepositoryName = "fakeRepo";

        when(this.githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                repositoryOwner, unavailableRepositoryName))
                .thenThrow(GithubRepositoryException.class);

        githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                repositoryOwner, unavailableRepositoryName);
    }

    @SuppressWarnings("unchecked")
    @Test (expected = GithubRepositoryException.class)
    public void whenNonExistingUserProvidedThenThrowsException() {
        String unavailableOwner = "fakeUser";

        when(this.githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(unavailableOwner, repositoryName))
                .thenThrow(GithubRepositoryException.class);

        githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                unavailableOwner, repositoryName);
    }

}
