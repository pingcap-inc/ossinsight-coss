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

import jakarta.persistence.*;

import java.util.Date;

/**
 * COSSTotalBean
 *
 * @author Icemap
 * @date 2022/11/07
 */
@Entity
@Table(name = "mv_coss_total")
public class COSSTotalBean {
    @Id
    @Column(name = "github_name")
    private String githubName;

    @Column(name = "contributors")
    private Long contributors;

    public String getGithubName() {
        return githubName;
    }

    public void setGithubName(String githubName) {
        this.githubName = githubName;
    }

    public Long getContributors() {
        return contributors;
    }

    public void setContributors(Long contributors) {
        this.contributors = contributors;
    }
}
