package com.pingcap.ossinsightcoss.controller;

import com.pingcap.ossinsightcoss.dao.COSSRepoRepository;
import com.pingcap.ossinsightcoss.github.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    GitHubRepository repository;

    @Autowired
    COSSRepoRepository cossRepoRepository;

    @GetMapping
    public Object test(String repo) throws IOException {
        System.out.println(repository.get(repo).getLicense());
        return cossRepoRepository.save(repository.convert(repository.get(repo)));
    }
}
