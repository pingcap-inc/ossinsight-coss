package com.pingcap.ossinsightcoss.dao;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class COSSDevMonthlyKey implements Serializable {
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
        if (o == null || getClass() != o.getClass()) return false;

        COSSDevMonthlyKey that = (COSSDevMonthlyKey) o;

        if (!Objects.equals(githubName, that.githubName)) return false;
        return Objects.equals(eventMonth, that.eventMonth);
    }

    @Override
    public int hashCode() {
        int result = githubName != null ? githubName.hashCode() : 0;
        result = 31 * result + (eventMonth != null ? eventMonth.hashCode() : 0);
        return result;
    }
}
