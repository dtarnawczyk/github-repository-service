package org.githubservice.integration;

import org.githubservice.Application;
import org.githubservice.controller.GithubRestController;
import org.githubservice.service.GithubRepositoryException;
import org.githubservice.model.GithubRepositoryModel;
import org.githubservice.service.GithubConsumer;
import org.githubservice.util.DateUtil;
import org.githubservice.util.MockGithubRepositoryModel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GithubRestControllerTest {

    @Rule
    public ExpectedException githubRepositoryException = ExpectedException.none();

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private GithubConsumer githubConsumer;

    @InjectMocks
    private GithubRestController githubRestController;

    private String repositoryOwner = "jquery";
    private String repositoryName = "jquery";
    private String githubRepositoryServicePath = "/repositories/" + repositoryOwner + "/" + repositoryName;

    @Test
    public void whenExistingOwnerAndRepositoryNameProvidedThenReturnRepositoryDetails() throws Exception {
        GithubRepositoryModel fakeRepositoryDetails = createGithubFakeRepositoryModel();
        this.mockMvc.perform(get(githubRepositoryServicePath).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullName", is(fakeRepositoryDetails.getFullName())))
            .andExpect(jsonPath("$.description", is(fakeRepositoryDetails.getDescription())))
            .andExpect(jsonPath("$.cloneUrl", is(fakeRepositoryDetails.getCloneUrl())))
            .andExpect(jsonPath("$.createdAt", is(getLocalizedDateFormat(
                    fakeRepositoryDetails.getCreatedAt()))));
    }

    @Test
    public void whenNotExistingOwnerOrRepositoryNameProvidedThenThrowException() throws Exception {
        githubRepositoryServicePath = "/repositories/fakeuser/fakerepo";
        this.mockMvc.perform(get(githubRepositoryServicePath).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.statusCode", is(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("$.message", is("GitHub repository or user not found")));
    }

    @Test
    public void whenNoExistingPathProvidedOrOtherServerErrorThenThrowException() throws Exception {
        initMockController();
        String errorMessage = "Internal Server Error";
        when(this.githubConsumer.getGithubRepositoryModelOnOwnerRepositoryName(
                any(), any()))
                .thenThrow(new GithubRepositoryException(errorMessage,
                        HttpStatus.SERVICE_UNAVAILABLE.value()));
        githubRepositoryException.expectMessage(errorMessage);
        githubRepositoryException.expectCause(org.hamcrest.Matchers.any(GithubRepositoryException.class));
        this.mockMvc.perform(get(githubRepositoryServicePath)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.statusCode", is(HttpStatus.SERVICE_UNAVAILABLE.value())))
            .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    private void initMockController() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.githubRestController).build();
    }

    private GithubRepositoryModel createGithubFakeRepositoryModel() {
        GithubRepositoryModel githubRepositoryModel = new MockGithubRepositoryModel.Builder()
                .setFullName("jquery/jquery")
                .setDescription("jQuery JavaScript Library")
                .setCloneUrl("https://github.com/jquery/jquery.git")
                .setStars(44470)
                .setCreatedAt(DateUtil.createDateFromIsoString("2009-04-03T15:20:14Z"))
                .build();
        return githubRepositoryModel;
    }

    private String getLocalizedDateFormat(Date date) {
        DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(date.toInstant().toString(),
                isoDateTimeFormatter);
        DateTimeFormatter localFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return localDateTime.format(localFormatter);
    }

}
