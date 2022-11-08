package com.pingcap.ossinsightcoss.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * COSSTotalRepository
 *
 * @author Icemap
 * @date 2022/11/07
 */
@Repository
public interface COSSTotalRepository extends JpaRepository<COSSTotalBean, String> {
    @Query(value = """
    SELECT
    	ge.repo_name AS `github_name`,
    	COUNT(DISTINCT ge.creator_user_id) AS `contributors`
    FROM github_events ge
    WHERE ge.`type` = 'PullRequestEvent'
    AND ge.`pr_merged` = 1
    AND ge.repo_name = :repo_name
    GROUP BY ge.repo_name
    """, nativeQuery = true)
    COSSTotalBean selectTotalBeanByRepoName(@Param("repo_name") String repoName);
}
