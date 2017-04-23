package org.githubservice.model;

import org.githubservice.util.DateUtil;
import org.githubservice.util.MockGithubRepositoryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class GithubRepositoryModelTest {

    @Autowired
    private JacksonTester<GithubRepositoryModel> json;

    @Test
    public void whenModelProvidedThenProperJsonCreated() throws Exception {
        String expectedDate = "2009-04-03T15:20:14Z";
        String localizedDateString = getLocalDateFormat(expectedDate);
        String expectedJson = "{"+
                "\"fullName\":\"jquery/jquery\"," +
                "\"description\":\"jQuery JavaScript Library\"," +
                "\"cloneUrl\":\"https://github.com/jquery/jquery.git\"," +
                "\"stars\":44470,"+
                "\"createdAt\":\""+localizedDateString+"\"" +
                "}";
        GithubRepositoryModel githubRepositoryModel = new MockGithubRepositoryModel.Builder()
                .setFullName("jquery/jquery")
                .setDescription("jQuery JavaScript Library")
                .setCloneUrl("https://github.com/jquery/jquery.git")
                .setStars(44470)
                .setCreatedAt(DateUtil.createDateFromIsoString(expectedDate))
                .build();
        assertThat(this.json.write(githubRepositoryModel).toString().contains(expectedJson));
    }

    @Test
    public void whenJsonFromGithubProvidedThenProperModelCreated() throws Exception {
        String jsonFromGithub = "{\n" +
                "  \"id\": 73920384,\n" +
                "  \"name\": \"modernlrs\",\n" +
                "  \"full_name\": \"dtarnawczyk/modernlrs\",\n" +
                "  \"owner\": {\n" +
                 "    \"type\": \"User\",\n" +
                "    \"site_admin\": false\n" +
                "  },\n" +
                "  \"private\": false,\n" +
                "  \"description\": \"Learning Record Store\",\n" +
                "  \"deployments_url\": \"https://api.github.com/repos/dtarnawczyk/modernlrs/deployments\",\n" +
                "  \"created_at\": \"2016-11-16T12:58:32Z\",\n" +
                "  \"ssh_url\": \"git@github.com:dtarnawczyk/modernlrs.git\",\n" +
                "  \"clone_url\": \"https://github.com/dtarnawczyk/modernlrs.git\",\n" +
                "  \"svn_url\": \"https://github.com/dtarnawczyk/modernlrs\",\n" +
                "  \"homepage\": null,\n" +
                "  \"size\": 5013,\n" +
                "  \"stargazers_count\": 0,\n" +
                "  \"default_branch\": \"master\",\n" +
                "  \"network_count\": 0,\n" +
                "  \"subscribers_count\": 0\n" +
                "}";

        String fullName = "dtarnawczyk/modernlrs";
        String description = "Learning Record Store";
        String cloneUrl = "https://github.com/dtarnawczyk/modernlrs.git";
        int stars = 0;
        Date expectedDate = DateUtil.createDateFromIsoString("2016-11-16T12:58:32Z");
        GithubRepositoryModel serializedModel = this.json.parseObject(jsonFromGithub);
        assertThat(serializedModel.getFullName()).isEqualTo(fullName);
        assertThat(serializedModel.getDescription()).isEqualTo(description);
        assertThat(serializedModel.getCloneUrl()).isEqualTo(cloneUrl);
        assertThat(serializedModel.getStars()).isEqualTo(stars);
        assertThat(serializedModel.getCreatedAt()).isEqualTo(expectedDate);
    }

    private String getLocalDateFormat(String dateString) {
        DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse("2009-04-03T15:20:14Z", isoDateTimeFormatter);
        DateTimeFormatter localFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return localDateTime.format(localFormatter);
    }

}
