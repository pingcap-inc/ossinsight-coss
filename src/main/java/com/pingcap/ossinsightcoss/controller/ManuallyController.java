package com.pingcap.ossinsightcoss.controller;

import com.pingcap.ossinsightcoss.Config;
import com.pingcap.ossinsightcoss.interval.DevBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/manually")
public class ManuallyController {

    @Autowired
    DevBuilder devBuilder;

    @Autowired
    Config config;

    // Refreshing runs heavy queries on every tracked repo, so it is disabled
    // unless coss.admin-token is configured and sent as the X-Admin-Token header.
    @PostMapping
    public ResponseEntity<String> refresh(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        String adminToken = config.getAdminToken();
        if (adminToken == null || adminToken.isEmpty() || token == null ||
                !MessageDigest.isEqual(adminToken.getBytes(StandardCharsets.UTF_8),
                        token.getBytes(StandardCharsets.UTF_8))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }

        devBuilder.addDevMonthlyDataTaskToStack();
        return ResponseEntity.ok("Done");
    }
}
