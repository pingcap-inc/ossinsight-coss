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

package com.pingcap.ossinsightcoss.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * COSSDevDailyRepository
 *
 * @author Icemap
 * @date 2022/10/24
 */
@Repository
public interface COSSDevDailyRepository extends JpaRepository<COSSDevDailyBean, Long> {
    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO mv_coss_dev_day (
        github_name, event_day, 
        event_num, star_num, 
        pr_num, issue_num, 
        dev_num, star_dev_num, 
        pr_dev_num, issue_dev_num
    )
    SELECT
        gr.repo_name                                                                     AS github_name,
        DATE(ge.created_at)                                                              AS raw_event_day,
        COUNT(*)                                                                         AS raw_event_num,
        COUNT(IF(ge.type = 'WatchEvent', 1, NULL))                                       AS raw_star_num,
        COUNT(IF(ge.type = 'PullRequestEvent', 1, NULL))                                 AS raw_pr_num,
        COUNT(IF(ge.type = 'IssuesEvent', 1, NULL))                                      AS raw_issue_num,
        COUNT(DISTINCT ge.actor_login)                                                   AS raw_dev_num,
        COUNT(DISTINCT IF(ge.type = 'WatchEvent', ge.actor_login, NULL))                 AS raw_star_dev_num,
        COUNT(DISTINCT IF(ge.type = 'PullRequestEvent', ge.actor_login, NULL))           AS raw_pr_dev_num,
        COUNT(DISTINCT IF(ge.type = 'IssuesEvent', ge.actor_login, NULL))                AS raw_issue_dev_num
    FROM github_events ge
    JOIN github_repos gr ON gr.repo_id = ge.repo_id
    WHERE ge.repo_id = (SELECT repo_id FROM github_repos WHERE repo_name = :repo_name)
        AND ge.type IN ('WatchEvent', 'PullRequestEvent', 'IssuesEvent')
        AND ge.action IN ('opened', 'created', 'started')
        AND ge.created_at < CURRENT_DATE()
        GROUP BY ge.repo_id, raw_event_day
    ON DUPLICATE KEY UPDATE 
        event_num = raw_event_num, star_num = raw_star_num, 
        pr_num = raw_pr_num, issue_num = raw_issue_num, 
        dev_num = raw_dev_num, star_dev_num = raw_star_dev_num, 
        pr_dev_num = raw_pr_dev_num, issue_dev_num = raw_issue_dev_num
    """, nativeQuery = true)
    Integer transferCOSSDevDailyBeanByRepoName(@Param("repo_name") String repoName);

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO mv_coss_dev_day (
        github_name, event_day, 
        event_num, star_num, 
        pr_num, issue_num, 
        dev_num, star_dev_num, 
        pr_dev_num, issue_dev_num
    )
    SELECT
        gr.repo_name                                                                     AS github_name,
        DATE(ge.created_at)                                                              AS raw_event_day,
        COUNT(*)                                                                         AS raw_event_num,
        COUNT(IF(ge.type = 'WatchEvent', 1, NULL))                                       AS raw_star_num,
        COUNT(IF(ge.type = 'PullRequestEvent', 1, NULL))                                 AS raw_pr_num,
        COUNT(IF(ge.type = 'IssuesEvent', 1, NULL))                                      AS raw_issue_num,
        COUNT(DISTINCT ge.actor_login)                                                   AS raw_dev_num,
        COUNT(DISTINCT IF(ge.type = 'WatchEvent', ge.actor_login, NULL))                 AS raw_star_dev_num,
        COUNT(DISTINCT IF(ge.type = 'PullRequestEvent', ge.actor_login, NULL))           AS raw_pr_dev_num,
        COUNT(DISTINCT IF(ge.type = 'IssuesEvent', ge.actor_login, NULL))                AS raw_issue_dev_num
    FROM github_events ge
    JOIN github_repos gr ON gr.repo_id = ge.repo_id
    WHERE ge.repo_id = (SELECT repo_id FROM github_repos WHERE repo_name = :repo_name)
        AND ge.type IN ('WatchEvent', 'PullRequestEvent', 'IssuesEvent')
        AND ge.action IN ('opened', 'created', 'started')
        AND ge.created_at < CURRENT_DATE()
        AND ge.created_at >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY)
        GROUP BY ge.repo_id, raw_event_day
    ON DUPLICATE KEY UPDATE 
        event_num = raw_event_num, star_num = raw_star_num, 
        pr_num = raw_pr_num, issue_num = raw_issue_num, 
        dev_num = raw_dev_num, star_dev_num = raw_star_dev_num, 
        pr_dev_num = raw_pr_dev_num, issue_dev_num = raw_issue_dev_num
    """, nativeQuery = true)
    Integer transferLatest30DaysCOSSDevDailyBeanByRepoName(@Param("repo_name") String repoName);
}
