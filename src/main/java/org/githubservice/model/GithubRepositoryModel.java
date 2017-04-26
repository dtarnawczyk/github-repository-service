package org.githubservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown=true)
public class GithubRepositoryModel {

    private final String fullName;
    private final String description;
    private final String cloneUrl;
    private final int stars;
    private final Date createdAt;

    @JsonCreator
    public GithubRepositoryModel(
            @JsonProperty("full_name") String fullName,
            @JsonProperty("description") String description,
            @JsonProperty("clone_url") String cloneUrl,
            @JsonProperty("stargazers_count") int stars,
            @JsonProperty("created_at") Date createdAt) {
        this.fullName = fullName;
        this.description = description;
        this.cloneUrl = cloneUrl;
        this.stars = stars;
        this.createdAt = createdAt;
    }

    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("cloneUrl")
    public String getCloneUrl() {
        return cloneUrl;
    }

    @JsonProperty("stars")
    public int getStars() {
        return stars;
    }

    @JsonProperty("createdAt")
    @JsonSerialize(using = DateSerializer.class)
    public Date getCreatedAt() {
        return createdAt;
    }
}
