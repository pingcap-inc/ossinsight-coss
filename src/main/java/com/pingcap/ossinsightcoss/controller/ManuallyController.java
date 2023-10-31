package com.pingcap.ossinsightcoss.controller;

import com.pingcap.ossinsightcoss.dao.COSSRepoRepository;
import com.pingcap.ossinsightcoss.interval.DevBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manually")
public class ManuallyController {

    @Autowired
    DevBuilder devBuilder;

    @Autowired
    COSSRepoRepository cossRepoRepository;

    @GetMapping
    public Object refresh(){
        devBuilder.addDevMonthlyDataTaskToStack();
        return "Done";
    }
}
