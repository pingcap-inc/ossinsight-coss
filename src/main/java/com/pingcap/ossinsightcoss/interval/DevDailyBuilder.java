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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DevDailyBuilder
 *
 * @author Icemap
 * @date 2022/10/24
 */
@Service
public class DevDailyBuilder {
    @Autowired
    COSSDevDailyRepository cossDevDailyRepository;

    @Autowired
    COSSDevMonthlyRepository cossDevMonthlyRepository;

    @Autowired
    COSSInvestRepository cossInvestRepository;

    @Autowired
    ConvertUtil convertUtil;

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

        for (String transfer: needTransfer) {
            cossDevDailyRepository.transferCOSSDevDailyBeanByRepoName(transfer);
            cossDevMonthlyRepository.transferCOSSDevMonthlyBeanByRepoName(transfer);
        }
    }

//    @Scheduled(fixedDelay=30, timeUnit=TimeUnit.SECONDS)
//    public void buildAndRefreshDevDailyOfRepo() {
//        if (refreshStack.isEmpty()) {
//            refreshStack.addAll(cossInvestRepository.findAll());
//            return;
//        }
//
//        cossDevDailyRepository.transferCOSSDevDailyBeanByRepoName(
//                refreshStack.pop().getGithubName()
//        );
//    }

    // every day, 01:10 start this job
    @Scheduled(cron = "0 10 1 * * *")
    public void buildLastDayDevDailyOfRepo() {
        System.out.println("01:10 reached");
    }
}
