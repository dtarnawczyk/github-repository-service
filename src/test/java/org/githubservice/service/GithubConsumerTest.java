package org.githubservice.service;

import org.githubservice.model.GithubRepositoryModel;
import org.githubservice.util.DateUtil;
import org.githubservice.util.MockGithubRepositoryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class GithubConsumerTest {

    @MockBean
    private GithubConsumer githubConsumer;

    private String repositoryOwner = "jquery";
    private String repositoryName = "jquery";

    @Test
    public void whenExistingUserAndRepositoryNameProvidedThenRetrieveRepositoryDetails() {
        LocalDateTime expectedDate = DateUtil.createDate("2009-04-03 15:20:14");
        GithubRepositoryModel mockGithubRepositoryModel = new MockGithubRepositoryModel.Builder()
                .setFullName("jquery/jquery")
                .setDescription("jQuery JavaScript Library")
                .setCloneUrl("https://github.com/jquery/jquery.git")
                .setStars(44470)
                .setCreatedAt(expectedDate)
                .build();
        when(this.githubConsumer.getRepositoryDetails(repositoryOwner, repositoryName))
                .thenReturn(mockGithubRepositoryModel);
        GithubRepositoryModel model = this.githubConsumer.getRepositoryDetails(
                repositoryOwner, repositoryName);
        assertThat("jquery/jquery").isEqualTo(model.getFullName());
        assertThat("jQuery JavaScript Library").isEqualTo(model.getDescription());
        assertThat("https://github.com/jquery/jquery.git").isEqualTo(model.getCloneUrl());
        assertThat(44470).isEqualTo(model.getStars());
        assertThat(expectedDate).isEqualByComparingTo(model.getCreatedAt());
    }

    @SuppressWarnings("unchecked")
    @Test (expected = GithubRepositoryException.class)
    public void whenNonExistingRepositoryNameProvidedThenThrowsException() {
        String unavailableRepositoryName = "fakeRepo";
        when(this.githubConsumer.getRepositoryDetails(
                repositoryOwner, unavailableRepositoryName))
                .thenThrow(GithubRepositoryException.class);
        githubConsumer.getRepositoryDetails(
                repositoryOwner, unavailableRepositoryName);
    }

    @SuppressWarnings("unchecked")
    @Test (expected = GithubRepositoryException.class)
    public void whenNonExistingUserProvidedThenThrowsException() {
        String unavailableOwner = "fakeUser";
        when(this.githubConsumer.getRepositoryDetails(unavailableOwner, repositoryName))
                .thenThrow(GithubRepositoryException.class);
        githubConsumer.getRepositoryDetails(
                unavailableOwner, repositoryName);
    }
}
