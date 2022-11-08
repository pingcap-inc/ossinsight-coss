package com.pingcap.ossinsightcoss.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * COSSRepoBean
 *
 * @author Icemap
 * @date 2022/11/07
 */
@Entity
@Table(name = "coss_repo")
public class COSSRepoBean {
    @Id
    @Column(name = "repo_id")
    private Long repoID;

    @Column(name = "repo_name")
    private String repoName;

    @Column(name = "owner_id")
    private Long ownerID;

    @Column(name = "owner_login")
    private String ownerLogin;

    @Column(name = "owner_is_org")
    private Boolean ownerIsOrg;

    @Column(name = "description")
    private String description;

    @Column(name = "primary_language")
    private String primaryLanguage;

    @Column(name = "license")
    private String license;

    @Column(name = "size")
    private Long size;

    @Column(name = "stars")
    private Long stars;

    @Column(name = "forks")
    private Long forks;

    @Column(name = "watchers")
    private Long watchers;

    @Column(name = "parent_repo_id")
    private Long parentRepoID;

    @Column(name = "is_fork")
    private Boolean isFork;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "latest_released_at")
    private Date latestReleasedAt;

    @Column(name = "pushed_at")
    private Date pushedAt;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public COSSRepoBean() {
    }

    public COSSRepoBean(Long repoID, String repoName, Long ownerID, String ownerLogin, Boolean ownerIsOrg, String description, String primaryLanguage, String license, Long size, Long stars, Long forks, Long watchers, Long parentRepoID, Boolean isFork, Boolean isArchived, Boolean isDeleted, Date latestReleasedAt, Date pushedAt, Date createdAt, Date updatedAt) {
        this.repoID = repoID;
        this.repoName = repoName;
        this.ownerID = ownerID;
        this.ownerLogin = ownerLogin;
        this.ownerIsOrg = ownerIsOrg;
        this.description = description;
        this.primaryLanguage = primaryLanguage;
        this.license = license;
        this.size = size;
        this.stars = stars;
        this.forks = forks;
        this.watchers = watchers;
        this.parentRepoID = parentRepoID;
        this.isFork = isFork;
        this.isArchived = isArchived;
        this.isDeleted = isDeleted;
        this.latestReleasedAt = latestReleasedAt;
        this.pushedAt = pushedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getRepoID() {
        return repoID;
    }

    public void setRepoID(Long repoID) {
        this.repoID = repoID;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public Boolean getOwnerIsOrg() {
        return ownerIsOrg;
    }

    public void setOwnerIsOrg(Boolean ownerIsOrg) {
        this.ownerIsOrg = ownerIsOrg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getStars() {
        return stars;
    }

    public void setStars(Long stars) {
        this.stars = stars;
    }

    public Long getForks() {
        return forks;
    }

    public void setForks(Long forks) {
        this.forks = forks;
    }

    public Long getParentRepoID() {
        return parentRepoID;
    }

    public void setParentRepoID(Long parentRepoID) {
        this.parentRepoID = parentRepoID;
    }

    public Boolean getFork() {
        return isFork;
    }

    public void setFork(Boolean fork) {
        isFork = fork;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Date getLatestReleasedAt() {
        return latestReleasedAt;
    }

    public void setLatestReleasedAt(Date latestReleasedAt) {
        this.latestReleasedAt = latestReleasedAt;
    }

    public Date getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(Date pushedAt) {
        this.pushedAt = pushedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getWatchers() {
        return watchers;
    }

    public void setWatchers(Long watchers) {
        this.watchers = watchers;
    }
}
