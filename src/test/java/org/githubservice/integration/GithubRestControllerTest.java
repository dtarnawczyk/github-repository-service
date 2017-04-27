package org.githubservice.integration;

import org.githubservice.Application;
import org.githubservice.controller.GithubServiceError;
import org.githubservice.model.GithubRepositoryModel;
import org.githubservice.util.DateUtil;
import org.githubservice.util.MockGithubRepositoryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenRepositoryAndOwnerProvidedThenReturnRepositoryDetails() {
        Map<String, String> inputParameters = new HashMap<>();
        inputParameters.put("owner", "jquery");
        inputParameters.put("repository", "jquery");
        ResponseEntity<GithubRepositoryModel> response= this.restTemplate.getForEntity(
                "/repositories/{owner}/{repository}",
                GithubRepositoryModel.class, inputParameters);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GithubRepositoryModel mockGithubRepositoryModel = createMockGithubRepositoryModel();
        assertThat(response.getBody().getFullName())
                .isEqualTo(mockGithubRepositoryModel.getFullName());
        assertThat(response.getBody().getDescription())
                .isEqualTo(mockGithubRepositoryModel.getDescription());
        assertThat(response.getBody().getCloneUrl())
                .isEqualTo(mockGithubRepositoryModel.getCloneUrl());
        assertThat(response.getBody().getStars())
                .isGreaterThanOrEqualTo(mockGithubRepositoryModel.getStars());
        assertThat(response.getBody().getCreatedAt())
                .isEqualTo(mockGithubRepositoryModel.getCreatedAt());
    }

    @Test
    public void whenUnknownOwnerOrRepositoryNameProvidedThenReturnErrorMessage() {
        Map<String, String> inputParameters = new HashMap<>();
        inputParameters.put("owner", "unknown");
        inputParameters.put("repository", "unknown");
        ResponseEntity<GithubServiceError> response= this.restTemplate.getForEntity(
                "/repositories/{owner}/{repository}",
                GithubServiceError.class, inputParameters);
        assertThat(HttpStatus.NOT_FOUND).isEqualTo(response.getStatusCode());
        assertThat(response.getBody().getMessage()).isEqualTo("GitHub repository or user not found");
    }

    private GithubRepositoryModel createMockGithubRepositoryModel() {
        GithubRepositoryModel githubRepositoryModel = new MockGithubRepositoryModel.Builder()
                .setFullName("jquery/jquery")
                .setDescription("jQuery JavaScript Library")
                .setCloneUrl("https://github.com/jquery/jquery.git")
                .setStars(44470)
                .setCreatedAt(DateUtil.createDate("2009-04-03 15:20:14"))
                .build();
        return githubRepositoryModel;
    }

}
