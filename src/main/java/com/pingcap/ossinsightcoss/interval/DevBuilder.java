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

import com.pingcap.ossinsightcoss.dao.COSSDevDailyRepository;
import com.pingcap.ossinsightcoss.dao.COSSDevMonthlyRepository;
import com.pingcap.ossinsightcoss.dao.COSSInvestBean;
import com.pingcap.ossinsightcoss.dao.COSSInvestRepository;
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
 * DevDailyBuilder
 *
 * @author Icemap
 * @date 2022/10/24
 */
@Service
public class DevBuilder {
    Logger logger = Logger.getLogger(DevBuilder.class.getName());

    @Autowired
    COSSDevDailyRepository cossDevDailyRepository;
    @Autowired
    COSSDevMonthlyRepository cossDevMonthlyRepository;
    @Autowired
    COSSInvestRepository cossInvestRepository;
    @Autowired
    ConvertUtil convertUtil;

    Stack<COSSInvestBean> refreshDevDailyStack = new Stack<>();
    Stack<COSSInvestBean> refreshDevMonthlyStack = new Stack<>();
    Stack<String> refreshRepoNameStack = new Stack<>();

    @PostConstruct
    public void buildDevDailyOfRepo() throws Exception {
        Set<Integer> investIDSetInDatabase = cossInvestRepository.findAll().stream()
                .map(COSSInvestBean::getId).collect(Collectors.toSet());
        List<COSSInvestBean> investListInCSV = convertUtil.readCOSSInvestBean();
        List<COSSInvestBean> needAdd = new LinkedList<>();

        for (COSSInvestBean investCSV : investListInCSV) {
            if (!investIDSetInDatabase.contains(investCSV.getId())) {
                needAdd.add(investCSV);
            }
        }
        cossInvestRepository.saveAll(needAdd);
        Set<String> needTransfer = needAdd.stream()
                .filter(o -> o.getHasGithub() && o.getHasRepo())
                .map(COSSInvestBean::getGithubName)
                .collect(Collectors.toSet());

        refreshRepoNameStack.addAll(needTransfer);

        addDevMonthlyDataTaskToStack();
        addDevDailyDataTaskToStack();
    }

    @Scheduled(fixedDelay=1, timeUnit=TimeUnit.MINUTES)
    public void pickOneNewRepoNeedToUpdate() {
        if (!refreshRepoNameStack.isEmpty()) {
            String transfer = refreshRepoNameStack.pop();
            cossDevDailyRepository.transferCOSSDevDailyBeanByRepoName(transfer);
            cossDevMonthlyRepository.transferCOSSDevMonthlyBeanByRepoName(transfer);
        }
    }

    /**
     * Daily developers data refresh
     */
    // every day, 01:10 start produce tasks
    @Scheduled(cron = "0 10 1 * * *")
    public void addDevDailyDataTaskToStack() {
        if (refreshDevDailyStack.isEmpty()) {
            refreshDevDailyStack.addAll(getRefreshRepoList());
        }
    }

    // between each task, it will have 30s break.
    @Scheduled(fixedDelay=30, timeUnit=TimeUnit.SECONDS)
    public void pickOneDevDailyDataTask() {
        if (!refreshDevDailyStack.isEmpty()) {
            COSSInvestBean invest = refreshDevDailyStack.pop();
            logger.info("start transfer daily" + invest.getGithubName());
            if (invest != null) {
                cossDevDailyRepository.transferCOSSDevDailyBeanByRepoName(
                        invest.getGithubName()
                );
            }
        }
    }

    /**
     * Monthly developers data refresh
     */
    // every month 1st and 2nd day, 00:10 start produce tasks
    @Scheduled(cron = "0 10 0 1,2 * *")
    public void addDevMonthlyDataTaskToStack() {
        if (refreshDevMonthlyStack.isEmpty()) {
            refreshDevMonthlyStack.addAll(getRefreshRepoList());
        }
    }

    // between each task, it will have 30s break.
    @Scheduled(fixedDelay=30, timeUnit= TimeUnit.SECONDS)
    public void pickOneDevMonthlyDataTask() {
        if (!refreshDevMonthlyStack.isEmpty()) {
            COSSInvestBean invest = refreshDevDailyStack.pop();
            logger.info("start transfer monthly" + invest.getGithubName());

            if (invest != null) {
                cossDevMonthlyRepository.transferCOSSDevMonthlyBeanByRepoName(
                        invest.getGithubName()
                );
            }
        }
    }

    public List<COSSInvestBean> getRefreshRepoList() {
        List<COSSInvestBean> refreshList = cossInvestRepository.findAll();
        // refresh tidb first
        refreshList.sort((repo1, repo2) -> {
            String githubName = repo1.getGithubName();
            return githubName != null && githubName.equals("pingcap/tidb") ? -1 : 1;
        });

        logger.info("refresh list: " + refreshList.stream().map(COSSInvestBean::getGithubName).collect(Collectors.joining(",")));

        return refreshList;
    }
}
