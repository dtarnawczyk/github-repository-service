package org.githubservice.integration;

import org.githubservice.Application;
import org.githubservice.service.GithubConsumer;
import org.githubservice.service.GithubRepositoryException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubInternalErrorTest {

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.api.retriesCounter}")
    private int retriesCounter;

    @Autowired
    private RestTemplate githubClientRestTemplate;

    @Autowired
    private GithubConsumer githubConsumer;

    private ClientHttpRequestFactory originalRequestFactory;

    @Before
    public void saveRequestFactory() {
        this.originalRequestFactory = this.githubClientRestTemplate.getRequestFactory();
    }

    @After
    public void restoreRequestFactory() {
        this.githubClientRestTemplate.setRequestFactory(this.originalRequestFactory);
    }

    @Test
    public void whenErrorOnGitHubServiceThenThenRetry() {
        String owner = "dojo";
        String repository = "dojo";
        String gitHubRepositoryPath = String.join("/", this.githubApiUrl, owner, repository);
        MockRestServiceServer server = MockRestServiceServer.bindTo(this.githubClientRestTemplate).build();
        server.expect(times(this.retriesCounter), requestTo(gitHubRepositoryPath))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        try {
            this.githubConsumer.getRepositoryDetails(owner, repository);
        } catch (GithubRepositoryException exception){
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        server.verify();
    }
}
