package com.pingcap.ossinsightcoss.interval;

import com.pingcap.ossinsightcoss.dao.COSSInvestBean;
import com.pingcap.ossinsightcoss.dao.COSSInvestRepository;
import com.pingcap.ossinsightcoss.dao.COSSTotalBean;
import com.pingcap.ossinsightcoss.dao.COSSTotalRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TotalBuilder
 *
 * @author Icemap
 * @date 2022/11/08
 */
@Service
public class TotalBuilder {
    Stack<String> refreshRepoNameStack = new Stack<>();

    @Autowired
    COSSInvestRepository cossInvestRepository;

    @Autowired
    COSSTotalRepository cossTotalRepository;

    @Scheduled(cron = "@daily")
    public void produceRefreshTasks() {
        if (refreshRepoNameStack.isEmpty()) {
            refreshRepoNameStack.addAll(
                    cossInvestRepository.findAll().stream()
                            .filter(o -> o.getHasGithub() && o.getHasRepo())
                            .map(COSSInvestBean::getGithubName)
                            .collect(Collectors.toSet())
            );
        }
    }

    // break 30 seconds and then pop a repo name, if stack not empty
    @Scheduled(fixedDelay=30, timeUnit= TimeUnit.SECONDS)
    public void consumeRefreshTask() {
        if (!refreshRepoNameStack.isEmpty()) {
            COSSTotalBean totalBean = cossTotalRepository
                    .selectTotalBeanByRepoName(refreshRepoNameStack.pop());
            if (totalBean != null) {
                cossTotalRepository.save(totalBean);
            }
        }
    }
}
