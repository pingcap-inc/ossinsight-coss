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
import org.springframework.transaction.annotation.Transactional;

/**
 * BaldertonMonthlyRepository
 *
 * @author Icemap
 * @date 2023/2/6
 */
public interface BaldertonMonthlyRepository extends JpaRepository<BaldertonMonthlyBean, Long> {
    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO mv_balderton_monthly (
        github_name, event_month, 
        star_num, pr_num, fork_num
    )
    SELECT
        ge.repo_name AS raw_github_name,
        ge.event_month AS raw_event_month,
    
        COUNT(CASE WHEN ge.type = "WatchEvent" THEN 1 ELSE NULL END) AS raw_star_num,
        COUNT(CASE WHEN ge.type = "PullRequestEvent" THEN 1 ELSE NULL END) AS raw_pr_num,
        COUNT(CASE WHEN ge.type = "ForkEvent" THEN 1 ELSE NULL END) AS raw_fork_num
    FROM github_events ge
    WHERE ge.repo_name = :repo_name
    AND ge.event_month != DATE_FORMAT(CURRENT_DATE(),'%Y-%m-01')
    AND ge.event_month >= DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 12 MONTH),'%Y-%m-01')
    GROUP BY ge.repo_name, ge.event_month
    ON DUPLICATE KEY UPDATE 
        star_num = raw_star_num, pr_num = raw_pr_num, fork_num = raw_fork_num
    """, nativeQuery = true)
    Integer transferBaldertonMonthlyBeanByRepoName(@Param("repo_name") String repoName);
}
