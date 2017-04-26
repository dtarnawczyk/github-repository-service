package org.githubservice.util;

import org.githubservice.model.GithubRepositoryModel;

import java.util.Date;

public class MockGithubRepositoryModel extends GithubRepositoryModel {

    public static class Builder {

        private String fullName;
        private String description;
        private String cloneUrl;
        private Integer stars;
        private Date createdAt;

        public Builder setFullName(String fullName){
            this.fullName = fullName;
            return this;
        }

        public Builder setDescription(String description){
            this.description = description;
            return this;
        }

        public Builder setCloneUrl(String cloneUrl){
            this.cloneUrl = cloneUrl;
            return this;
        }

        public Builder setStars(Integer stars){
            this.stars = stars;
            return this;
        }

        public Builder setCreatedAt(Date createdAt){
            this.createdAt = createdAt;
            return this;
        }

        public MockGithubRepositoryModel build() {
            return new MockGithubRepositoryModel(fullName, description, cloneUrl, stars, createdAt);
        }
    }

    private MockGithubRepositoryModel(String fullName,
                                      String description,
                                      String cloneUrl,
                                      int stars,
                                      Date createdAt) {
        super(fullName, description, cloneUrl, stars, createdAt);
    }
}
