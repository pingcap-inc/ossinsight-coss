package com.pingcap.ossinsightcoss.interval;

import com.pingcap.ossinsightcoss.dao.COSSInvestBean;
import com.pingcap.ossinsightcoss.dao.COSSInvestRepository;
import com.pingcap.ossinsightcoss.dao.COSSRepoBean;
import com.pingcap.ossinsightcoss.dao.COSSRepoRepository;
import com.pingcap.ossinsightcoss.github.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * DevDailyBuilder
 *
 * @author Icemap
 * @date 2022/11/07
 */
@Service
public class RepoBuilder {
    @Autowired
    COSSInvestRepository cossInvestRepository;
    @Autowired
    COSSRepoRepository cossRepoRepository;
    @Autowired
    GitHubRepository gitHubRepository;

    Stack<String> refreshRepoNameStack = new Stack<>();

    @Scheduled(cron = "@hourly")
    public void produceRefreshTasks() {
        if (refreshRepoNameStack.isEmpty()) {
            List<COSSInvestBean> investBeanList = cossInvestRepository.findAll();
            Collections.shuffle(investBeanList);
            refreshRepoNameStack.addAll(
                    investBeanList.stream()
                            .filter(o -> o.getHasGithub() && o.getHasRepo())
                            .map(COSSInvestBean::getGithubName)
                            .collect(Collectors.toSet())
            );
        }
    }

    // break 5 seconds and then pop a repo name, if stack not empty
    @Scheduled(fixedDelay=5, timeUnit= TimeUnit.SECONDS)
    public void consumeRefreshTask() {
        if (!refreshRepoNameStack.isEmpty()) {
            cossRepoRepository.save(
                    gitHubRepository.convert(
                            gitHubRepository.get(refreshRepoNameStack.pop())
                    )
            );
        }
    }
}
