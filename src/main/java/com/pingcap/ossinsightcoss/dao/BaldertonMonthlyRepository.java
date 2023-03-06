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
    WITH monthes AS (
        SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 12 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 11 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 10 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 9 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 8 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 7 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 5 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 4 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 3 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 2 MONTH),'%Y-%m-01') AS m
        UNION ALL SELECT DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH),'%Y-%m-01') AS m
    ), monthly AS (
        SELECT
            ge.repo_name AS github_name,
            ge.event_month AS event_month,
            COUNT(CASE WHEN ge.type = "WatchEvent" THEN 1 ELSE NULL END) AS star_num,
            COUNT(CASE WHEN ge.type = "PullRequestEvent" THEN 1 ELSE NULL END) AS pr_num,
            COUNT(CASE WHEN ge.type = "ForkEvent" THEN 1 ELSE NULL END) AS fork_num
        FROM github_events ge
        WHERE ge.repo_name = :repo_name
        AND ge.event_month != DATE_FORMAT(CURRENT_DATE(),'%Y-%m-01')
        GROUP BY ge.repo_name, ge.event_month
    )
    SELECT
        monthly.github_name AS raw_github_name,
        monthes.m,
        SUM(monthly.star_num) AS raw_star_num,
        SUM(monthly.pr_num) AS raw_pr_num,
        SUM(monthly.fork_num) AS raw_fork_num
    FROM monthly JOIN monthes
    WHERE monthes.m >= monthly.event_month
    GROUP BY monthes.m
    ORDER BY monthly.github_name, monthes.m
    ON DUPLICATE KEY UPDATE 
        star_num = raw_star_num, pr_num = raw_pr_num, fork_num = raw_fork_num
    """, nativeQuery = true)
    Integer transferBaldertonMonthlyBeanByRepoName(@Param("repo_name") String repoName);
}
