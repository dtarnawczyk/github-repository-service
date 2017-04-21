package org.githubservice.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class GithubRepositoryModelTest {

    @Autowired
    private JacksonTester<GithubRepositoryModel> json;

    private String expectedJson = "{\n" +
            "  \"fullName\": \"jquery/jquery\",\n" +
            "  \"description\": \"jQuery JavaScript Library\",\n" +
            "  \"cloneUrl\": \"https://github.com/jquery/jquery.git\",\n" +
            "  \"stars\": 44470,\n" +
            "  \"createdAt\": \"2009-04-03T15:20:14Z\"\n" +
            "}";

    @Test
    public void whenModelProvidedThenProperJsonCreated() throws Exception {

        GithubRepositoryModel githubRepositoryModel = new GithubRepositoryModel();
        githubRepositoryModel.setFullName("jquery/jquery");
        githubRepositoryModel.setDescription("jQuery JavaScript Library");
        githubRepositoryModel.setCloneUrl("https://github.com/jquery/jquery.git");
        githubRepositoryModel.setStars(44470);
        githubRepositoryModel.setCreatedAt("2009-04-03T15:20:14Z");
        assertThat(this.json.write(githubRepositoryModel)).isEqualToJson(expectedJson);
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
        String createdAt = "2016-11-16T12:58:32Z";

        GithubRepositoryModel expectedGithubRepositoryModel = new GithubRepositoryModel();
        expectedGithubRepositoryModel.setFullName(fullName);
        expectedGithubRepositoryModel.setDescription(description);
        expectedGithubRepositoryModel.setCloneUrl(cloneUrl);
        expectedGithubRepositoryModel.setStars(stars);
        expectedGithubRepositoryModel.setCreatedAt(createdAt);
        
        assertThat(this.json.parseObject(jsonFromGithub).getFullName()).isEqualTo(fullName);
        assertThat(this.json.parseObject(jsonFromGithub).getDescription()).isEqualTo(description);
        assertThat(this.json.parseObject(jsonFromGithub).getCloneUrl()).isEqualTo(cloneUrl);
        assertThat(this.json.parseObject(jsonFromGithub).getStars()).isEqualTo(stars);
        assertThat(this.json.parseObject(jsonFromGithub).getCreatedAt()).isEqualTo(createdAt);
    }


}
