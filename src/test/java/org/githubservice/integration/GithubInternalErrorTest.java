package org.githubservice.integration;

import org.githubservice.Application;
import org.githubservice.controller.GithubServiceError;
import org.githubservice.service.GithubConsumer;
import org.githubservice.service.GithubRepositoryException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubInternalErrorTest {

    @MockBean
    private GithubConsumer githubConsumer;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenErrorOnGitHubServiceThenReturnErrorMessage() {
        String errorMessage = "Internal Server Error";
        when(this.githubConsumer.getRepositoryDetails(
                "dojo", "dojo"))
                .thenThrow(new GithubRepositoryException(errorMessage,
                        HttpStatus.SERVICE_UNAVAILABLE.value()));
        ResponseEntity<GithubServiceError> response= this.restTemplate.getForEntity(
                "/repositories/dojo/dojo", GithubServiceError.class);
        assertThat(HttpStatus.SERVICE_UNAVAILABLE).isEqualTo(response.getStatusCode());
        assertThat(response.getBody().getMessage()).isEqualTo(errorMessage);
    }
}
