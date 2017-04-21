package org.githubservice.integration;

import org.githubservice.Application;
import org.githubservice.model.GithubRepositoryModel;
import org.githubservice.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GithubRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String repositoryOwner = "jquery";
    private String repositoryName = "jquery";
    private String githubRepositoryServicePath = "/repositories/" + repositoryOwner + "/" + repositoryName;

    @Test
    public void whenExistingOwnerAndRepositoryNameProvidedThenReturnRepositoryDetails() throws Exception {

        GithubRepositoryModel fakeRepositoryDetails = new GithubRepositoryModel();
        fakeRepositoryDetails.setFullName("jquery/jquery");
        fakeRepositoryDetails.setDescription("jQuery JavaScript Library");
        fakeRepositoryDetails.setCloneUrl("https://github.com/jquery/jquery.git");
        fakeRepositoryDetails.setCreatedAt(DateUtil.createDateFromIsoString("2009-04-03T15:20:14Z"));

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

        githubRepositoryServicePath = "/repositories/testuser/testrepo";
        this.mockMvc.perform(get(githubRepositoryServicePath).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.statusCode", is(404)))
            .andExpect(jsonPath("$.message", is("GitHub repository or user not found")));
    }

    private String getLocalizedDateFormat(Date date) {
        DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(date.toInstant().toString(),
                isoDateTimeFormatter);
        DateTimeFormatter localFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return localDateTime.format(localFormatter);
    }

}
