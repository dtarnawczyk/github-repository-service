package org.githubservice.model;

import org.githubservice.util.DateUtil;
import org.githubservice.util.MockGithubRepositoryModel;
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

    @Test
    public void modelSerializationTest() throws Exception {
        GithubRepositoryModel githubRepositoryModel = createGithubFakeRepositoryModel();
        assertThat(this.json.write(githubRepositoryModel))
                .extractingJsonPathStringValue("$.fullName").isEqualTo("dojo/dojo");
        assertThat(this.json.write(githubRepositoryModel))
                .extractingJsonPathStringValue("$.description")
                .isEqualTo("Dojo 1 - the Dojo 1 toolkit core library. " +
                        "Please submit bugs to https://bugs.dojotoolkit.org/");
        assertThat(this.json.write(githubRepositoryModel))
                .extractingJsonPathStringValue("$.cloneUrl").isEqualTo("https://github.com/dojo/dojo.git");
        assertThat(this.json.write(githubRepositoryModel))
                .extractingJsonPathNumberValue("$.stars").isEqualTo(1019);
        assertThat(this.json.write(githubRepositoryModel))
                .extractingJsonPathStringValue("$.createdAt").isEqualTo("2013-05-19 19:50:18");
    }

    @Test
    public void modelDeserializationTest() throws Exception {
        String jsonModel = "{\n" +
                "  \"description\": \"Dojo 1 - the Dojo 1 toolkit core library. Please submit bugs to https://bugs.dojotoolkit.org/\",\n" +
                "  \"fullName\": \"dojo/dojo\",\n" +
                "  \"cloneUrl\": \"https://github.com/dojo/dojo.git\",\n" +
                "  \"stars\": 1019,\n" +
                "  \"createdAt\": \"2013-05-19 19:50:18\"\n" +
                "}";
        GithubRepositoryModel deserializedModel = this.json.parseObject(jsonModel);
        GithubRepositoryModel expectedModel = createGithubFakeRepositoryModel();
        assertThat(deserializedModel).isEqualToComparingFieldByField(expectedModel);
    }

    private GithubRepositoryModel createGithubFakeRepositoryModel() {
        GithubRepositoryModel githubRepositoryModel = new MockGithubRepositoryModel.Builder()
                .setFullName("dojo/dojo")
                .setDescription("Dojo 1 - the Dojo 1 toolkit core library. Please submit bugs to https://bugs.dojotoolkit.org/")
                .setCloneUrl("https://github.com/dojo/dojo.git")
                .setStars(1019)
                .setCreatedAt(DateUtil.createDate("2013-05-19 19:50:18"))
                .build();
        return githubRepositoryModel;
    }

}
