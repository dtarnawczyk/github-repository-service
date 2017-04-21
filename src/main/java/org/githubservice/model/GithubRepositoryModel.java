package org.githubservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({ "fullName", "description", "cloneUrl", "stars", "createdAt" })
public class GithubRepositoryModel {

    private String fullName;
    private String description;
    private String cloneUrl;
    private Integer stars = 0;
    private String createdAt = null;

    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }

    @JsonProperty("full_name")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("cloneUrl")
    public String getCloneUrl() {
        return cloneUrl;
    }

    @JsonProperty("clone_url")
    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    @JsonProperty("stars")
    public int getStars() {
        return stars;
    }

    @JsonProperty("stargazers_count")
    public void setStars(int stars) {
        this.stars = stars;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
