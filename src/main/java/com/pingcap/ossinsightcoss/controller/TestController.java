package com.pingcap.ossinsightcoss.controller;

import com.pingcap.ossinsightcoss.github.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    GitHubRepository repository;

    @GetMapping
    public Object test() {
        return repository.convert(repository.get("prestodb/presto"));
    }
}
