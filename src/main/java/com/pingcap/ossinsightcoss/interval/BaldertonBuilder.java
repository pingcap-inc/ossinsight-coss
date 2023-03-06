// Copyright 2022 PingCAP, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.pingcap.ossinsightcoss.interval;

import com.pingcap.ossinsightcoss.dao.BaldertonMonthlyRepository;
import com.pingcap.ossinsightcoss.dao.BaldertonTrackedBean;
import com.pingcap.ossinsightcoss.dao.BaldertonTrackedRepository;
import com.pingcap.ossinsightcoss.util.ConvertUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * BaldertonBuilder
 *
 * @author Icemap
 * @date 2023/2/6
 */
@Service
public class BaldertonBuilder {
    Logger logger = Logger.getLogger(BaldertonBuilder.class.getName());

    @Autowired
    BaldertonMonthlyRepository baldertonMonthlyRepository;
    @Autowired
    BaldertonTrackedRepository baldertonTrackedRepository;
    @Autowired
    ConvertUtil convertUtil;

    Stack<BaldertonTrackedBean> refreshStack = new Stack<>();

    @PostConstruct
    public void buildDevDailyOfRepo() throws Exception {
        Set<String> trackedIDSetInDatabase = baldertonTrackedRepository.findAll().stream()
                .map(BaldertonTrackedBean::getRepoName).collect(Collectors.toSet());
        List<BaldertonTrackedBean> trackedListInCSV = convertUtil.readBaldertonTrackedBean();
        Set<BaldertonTrackedBean> needAdd = new HashSet<>();

        for (BaldertonTrackedBean trackedCSV : trackedListInCSV) {
            if (!trackedIDSetInDatabase.contains(trackedCSV.getRepoName())) {
                needAdd.add(trackedCSV);
            }
        }
        baldertonTrackedRepository.saveAll(needAdd);
        refreshStack.addAll(needAdd);
    }

//    /**
//     * Daily balderton data refresh
//     */
//    // every day, 02:10 start produce tasks
//    @Scheduled(cron = "0 10 2 * * *")
//    public void addBaldertonTrackedToStack() {
//        if (refreshStack.isEmpty()) {
//            refreshStack.addAll(baldertonTrackedRepository.findAll());
//        }
//    }
//
//    @Scheduled(fixedDelay=30, timeUnit= TimeUnit.SECONDS)
//    public void buildAndRefreshMonthlyOfRepo() {
//        if (!refreshStack.isEmpty()) {
//            String repoName = refreshStack.pop().getRepoName();
//            logger.info("start transfer " + repoName);
//            baldertonMonthlyRepository.transferBaldertonMonthlyBeanByRepoName(repoName);
//        }
//    }

    @Scheduled(fixedDelay=30, timeUnit= TimeUnit.SECONDS)
    public void buildAndRefreshMonthlyOfRepo() {
        if (refreshStack.isEmpty()) {
            List<BaldertonTrackedBean> baldertonRepos = baldertonTrackedRepository.findAll();
            Collections.shuffle(baldertonRepos);
            logger.info("get " + baldertonRepos.size() + " repos to refresh");
            refreshStack.addAll(baldertonRepos);
            return;
        }

        String repoName = refreshStack.pop().getRepoName();
        logger.info("start transfer " + repoName);
        baldertonMonthlyRepository.transferBaldertonMonthlyBeanByRepoName(repoName);
    }
}
