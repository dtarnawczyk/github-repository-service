package org.githubservice.integration;

import org.githubservice.Application;
import org.githubservice.controller.GithubServiceError;
import org.githubservice.domain.GithubRepositoryModel;
import org.githubservice.util.DateUtil;
import org.githubservice.util.MockGithubRepositoryModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${github.api.url}")
    private String githubApiUrl;

    private Locale currentLocale;

    @Before
    public void saveLocale(){
        this.currentLocale = LocaleContextHolder.getLocale();
    }

    @After
    public void restoreLocale(){
        LocaleContextHolder.setLocale(this.currentLocale);
    }

    @Test
    public void whenRepositoryAndOwnerProvidedThenReturnRepositoryDetails() {
        Map<String, String> inputParameters = new HashMap<>();
        inputParameters.put("owner", "jquery");
        inputParameters.put("repository", "jquery");
        ResponseEntity<GithubRepositoryModel> response= this.restTemplate.getForEntity(
                "/repositories/{owner}/{repository}",
                GithubRepositoryModel.class, inputParameters);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GithubRepositoryModel mockGithubRepositoryModel =
                createMockGithubRepositoryModelWithLocaleFormatDate("2009-04-03 15:20:14");
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
    @SuppressWarnings("unchecked")
    public void whenLocalesChangedToFrenchThenDateTimeFormatChange () {
        Locale frenchLocales = new Locale("fr", "FR");
        LocaleContextHolder.setLocale(frenchLocales);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set("Accept-Language", "fr-FR");
        Map<String, String> inputParameters = new HashMap<>();
        inputParameters.put("owner", "jquery");
        inputParameters.put("repository", "jquery");
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<GithubRepositoryModel> response =
                this.restTemplate.exchange("/repositories/{owner}/{repository}",
                HttpMethod.GET, entity, GithubRepositoryModel.class, inputParameters);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        GithubRepositoryModel mockGithubRepositoryModel =
                createMockGithubRepositoryModelWithLocaleFormatDate("3 avr. 2009 15:20:14");
        assertThat(response.getBody().getCreatedAt())
                .isEqualTo(mockGithubRepositoryModel.getCreatedAt());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenLocalesChangedToSpanishThenDateTimeFormatChange () {
        Locale frenchLocales = new Locale("es", "ES");
        LocaleContextHolder.setLocale(frenchLocales);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set("Accept-Language", "es-ES");
        Map<String, String> inputParameters = new HashMap<>();
        inputParameters.put("owner", "jquery");
        inputParameters.put("repository", "jquery");
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<GithubRepositoryModel> response =
                this.restTemplate.exchange("/repositories/{owner}/{repository}",
                        HttpMethod.GET, entity, GithubRepositoryModel.class, inputParameters);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        GithubRepositoryModel mockGithubRepositoryModel =
                createMockGithubRepositoryModelWithLocaleFormatDate("03-abr-2009 15:20:14");
        assertThat(response.getBody().getCreatedAt())
                .isEqualTo(mockGithubRepositoryModel.getCreatedAt());
    }

    @Test
    public void whenUnknownOwnerOrRepositoryNameProvidedThenReturnErrorMessage() {
        Map<String, String> inputParameters = new HashMap<>();
        inputParameters.put("owner", "unknown");
        inputParameters.put("repository", "unknown");
        ResponseEntity<GithubServiceError> response = this.restTemplate.getForEntity(
                "/repositories/{owner}/{repository}",
                GithubServiceError.class, inputParameters);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("GitHub repository or user not found");
    }

    private GithubRepositoryModel createMockGithubRepositoryModelWithLocaleFormatDate(String dateTime) {
        GithubRepositoryModel githubRepositoryModel = new MockGithubRepositoryModel.Builder()
                .setFullName("jquery/jquery")
                .setDescription("jQuery JavaScript Library")
                .setCloneUrl("https://github.com/jquery/jquery.git")
                .setStars(44470)
                .setCreatedAt(DateUtil.createDate(dateTime))
                .build();
        return githubRepositoryModel;
    }

}
