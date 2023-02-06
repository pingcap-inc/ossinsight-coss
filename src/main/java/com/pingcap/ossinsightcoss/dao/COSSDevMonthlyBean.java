package com.pingcap.ossinsightcoss.dao;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "mv_coss_dev_month")
@IdClass(value = COSSDevMonthlyKey.class)
public class COSSDevMonthlyBean {
    @Id
    @Column(name = "github_name")
    private String githubName;
    @Id
    @Column(name = "event_month")
    private Date eventMonth;

    // event fields
    @Column(name = "event_num")
    private Integer eventNum;
    @Column(name = "star_num")
    private Integer starNum;
    @Column(name = "pr_num")
    private Integer prNum;
    @Column(name = "issue_num")
    private Integer issueNum;

    // dev fields
    @Column(name = "dev_num")
    private Integer devNum;
    @Column(name = "star_dev_num")
    private Integer starDevNum;
    @Column(name = "pr_dev_num")
    private Integer prDevNum;
    @Column(name = "issue_dev_num")
    private Integer issueDevNum;

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

    public Integer getEventNum() {
        return eventNum;
    }

    public void setEventNum(Integer eventNum) {
        this.eventNum = eventNum;
    }

    public Integer getStarNum() {
        return starNum;
    }

    public void setStarNum(Integer starNum) {
        this.starNum = starNum;
    }

    public Integer getPrNum() {
        return prNum;
    }

    public void setPrNum(Integer prNum) {
        this.prNum = prNum;
    }

    public Integer getIssueNum() {
        return issueNum;
    }

    public void setIssueNum(Integer issueNum) {
        this.issueNum = issueNum;
    }

    public Integer getDevNum() {
        return devNum;
    }

    public void setDevNum(Integer devNum) {
        this.devNum = devNum;
    }

    public Integer getStarDevNum() {
        return starDevNum;
    }

    public void setStarDevNum(Integer starDevNum) {
        this.starDevNum = starDevNum;
    }

    public Integer getPrDevNum() {
        return prDevNum;
    }

    public void setPrDevNum(Integer prDevNum) {
        this.prDevNum = prDevNum;
    }

    public Integer getIssueDevNum() {
        return issueDevNum;
    }

    public void setIssueDevNum(Integer issueDevNum) {
        this.issueDevNum = issueDevNum;
    }

    public String toCSVLine() {
        return this.getGithubName() + "," +
                this.getEventMonth() + "," +
                this.getEventNum() + "," +
                this.getStarNum() + "," +
                this.getPrNum() + "," +
                this.getIssueNum() + "," +
                this.getDevNum() + "," +
                this.getStarDevNum() + "," +
                this.getPrDevNum() + "," +
                this.getIssueDevNum();
    }

    public static String getCSVHeader() {
        return "github_name,event_month,event_num,star_num,pr_num,issue_num,dev_num," +
                "star_dev_num,pr_dev_num,issue_dev_num";
    }
}
