package com.pingcap.ossinsightcoss.github;

import com.pingcap.ossinsightcoss.Config;
import com.pingcap.ossinsightcoss.dao.COSSRepoBean;
import jakarta.annotation.PostConstruct;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class GitHubRepository {
    private GitHub github;

    @Autowired
    Config config;

    @PostConstruct
    public void init () throws IOException {
        github = new GitHubBuilder()
                .withJwtToken(config.getGithubToken())
                .build();
    }

    public GHRepository get(String name) {
        try {
            return github.getRepository(name);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public COSSRepoBean convert(GHRepository ghRepo) {
        GHUser owner = null;
        Boolean ownerIsOrg = false;
        String license = "";
        Long parentRepoID = 0L;
        Date latestReleasedAt = null;
        Date createdAt = null;
        Date updatedAt = null;

        try {
            owner = ghRepo.getOwner();
            ownerIsOrg = owner.getType().equalsIgnoreCase("Organization");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            license = ghRepo.getLicense() == null ? null : ghRepo.getLicense().getName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            parentRepoID = ghRepo.getParent() == null ? null : ghRepo.getParent().getId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            latestReleasedAt = ghRepo.getLatestRelease() == null ? null : ghRepo.getLatestRelease().getPublished_at();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            latestReleasedAt = ghRepo.getLatestRelease().getPublished_at();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            createdAt = ghRepo.getCreatedAt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            updatedAt = ghRepo.getUpdatedAt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new COSSRepoBean(
                ghRepo.getId(),
                ghRepo.getFullName(),
                owner == null ? null : owner.getId(),
                owner == null ? null : owner.getLogin(),
                ownerIsOrg,
                ghRepo.getDescription(),
                ghRepo.getLanguage(),
                license,
                (long) ghRepo.getSize(),
                (long) ghRepo.getStargazersCount(),
                (long) ghRepo.getForksCount(),
                parentRepoID,
                ghRepo.isFork(),
                ghRepo.isArchived(),
                ghRepo.isDisabled(),
                latestReleasedAt,
                ghRepo.getPushedAt(),
                createdAt,
                updatedAt
        );
    }
}
