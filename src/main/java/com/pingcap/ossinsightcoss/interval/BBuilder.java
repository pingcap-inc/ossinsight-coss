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

import com.pingcap.ossinsightcoss.dao.BMonthlyRepository;
import com.pingcap.ossinsightcoss.dao.BTrackedBean;
import com.pingcap.ossinsightcoss.dao.BTrackedRepository;
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
 * BBuilder
 *
 * @author Icemap
 * @date 2023/2/6
 */
@Service
public class BBuilder {
    Logger logger = Logger.getLogger(BBuilder.class.getName());

    @Autowired
    BMonthlyRepository bMonthlyRepository;
    @Autowired
    BTrackedRepository bTrackedRepository;
    @Autowired
    ConvertUtil convertUtil;

    Stack<BTrackedBean> refreshStack = new Stack<>();

    @PostConstruct
    public void buildDevDailyOfRepo() throws Exception {
        Set<String> trackedIDSetInDatabase = bTrackedRepository.findAll().stream()
                .map(BTrackedBean::getRepoName).collect(Collectors.toSet());
        List<BTrackedBean> trackedListInCSV = convertUtil.readBTrackedBean();
        Set<BTrackedBean> needAdd = new HashSet<>();

        for (BTrackedBean trackedCSV : trackedListInCSV) {
            if (!trackedIDSetInDatabase.contains(trackedCSV.getRepoName())) {
                needAdd.add(trackedCSV);
            }
        }
        bTrackedRepository.saveAll(needAdd);
        refreshStack.addAll(needAdd);
    }

//    /**
//     * Daily b data refresh
//     */
//    // every day, 02:10 start produce tasks
//    @Scheduled(cron = "0 10 2 * * *")
//    public void addBTrackedToStack() {
//        if (refreshStack.isEmpty()) {
//            refreshStack.addAll(bTrackedRepository.findAll());
//        }
//    }
//
//    @Scheduled(fixedDelay=30, timeUnit= TimeUnit.SECONDS)
//    public void buildAndRefreshMonthlyOfRepo() {
//        if (!refreshStack.isEmpty()) {
//            String repoName = refreshStack.pop().getRepoName();
//            logger.info("start transfer " + repoName);
//            bMonthlyRepository.transferBMonthlyBeanByRepoName(repoName);
//        }
//    }

    @Scheduled(fixedDelay=30, timeUnit= TimeUnit.SECONDS)
    public void buildAndRefreshMonthlyOfRepo() {
        if (refreshStack.isEmpty()) {
            List<BTrackedBean> bRepos = bTrackedRepository.findAll();
            Collections.shuffle(bRepos);
            logger.info("get " + bRepos.size() + " repos to refresh");
            refreshStack.addAll(bRepos);
            return;
        }

        String repoName = refreshStack.pop().getRepoName();
        logger.info("start transfer " + repoName);
        bMonthlyRepository.transferBMonthlyBeanByRepoName(repoName);
    }
}
