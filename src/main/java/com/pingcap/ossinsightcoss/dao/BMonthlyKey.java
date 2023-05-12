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

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * BMonthlyKey
 *
 * @author Icemap
 * @date 2023/2/6
 */
public class BMonthlyKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String githubName;
    private Date eventMonth;

    public String getGithubName() {
        return githubName;
    }

    public void setGithubName(String githubName) {
        this.githubName = githubName;
    }

    public Date getEventMonth() {
        return eventMonth;
    }

    public void setEventMonth(Date eventMonth) {
        this.eventMonth = eventMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BMonthlyKey that)) return false;

        if (getGithubName() != null ? !getGithubName().equals(that.getGithubName()) : that.getGithubName() != null)
            return false;
        return getEventMonth() != null ? getEventMonth().equals(that.getEventMonth()) : that.getEventMonth() == null;
    }

    @Override
    public int hashCode() {
        int result = getGithubName() != null ? getGithubName().hashCode() : 0;
        result = 31 * result + (getEventMonth() != null ? getEventMonth().hashCode() : 0);
        return result;
    }
}

