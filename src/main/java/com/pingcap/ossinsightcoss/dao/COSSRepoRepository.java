package com.pingcap.ossinsightcoss.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * COSSRepoRepository
 *
 * @author Icemap
 * @date 2022/11/07
 */
@Repository
public interface COSSRepoRepository extends JpaRepository<COSSRepoBean, Long> {
}
