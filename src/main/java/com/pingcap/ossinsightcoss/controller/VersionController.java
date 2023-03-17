package com.pingcap.ossinsightcoss.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/version")
public class VersionController {

    Date startTime = new Date();
    @PostConstruct
    public void init() {
        startTime = new Date();
    }

    @GetMapping
    public Date getVersion() {
        return startTime;
    }
}
