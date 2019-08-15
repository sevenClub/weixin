package com.yangmj.entity;

import com.yangmj.entity.base.BaseEntity;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class OrderItem extends BaseEntity {
    private Integer id;

    private String projectId;

    private Integer currNum;

    private Integer totalNum;

    private String isFull;

    private String gameStatus;

    //作为查询条件的结果金额的上限
    private BigDecimal endPrice;

    private String gameLocation;

    private String orderStatus;

    private Date createTime;

    private String updateTime;

    private String actureStartTm;

    private String endTime;

    private String sportTitle;

    public HashMap getSort() {
        return sort;
    }

    public void setSort(HashMap sort) {
        this.sort = sort;
    }

    private BigDecimal projectCost;

    private String feeTags;

    private BigDecimal activityHour;

    private String description;

    private String contactDir;
    //查询出id，是用户的details的
    private String wechatOpenid;

    private boolean joined;

    private HashMap sort;

    private String perCost;
    private String sportImgUrl;
    private String sportType;
    private String firstPageUrl;
    //新增字段，不映射到数据库
    @Transient
    private String numType;
    @Transient
    private String costRMB;

    @Transient
    private String queryDate;

    /**
     * 查询的总人数上限
     */
    @Transient
    private Integer totalNumUp;

    public String getFirstPageUrl() {
        return firstPageUrl;
    }

    public void setFirstPageUrl(String firstPageUrl) {
        this.firstPageUrl = firstPageUrl;
    }

    private String startWechatOpenid;

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }


    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public void setWechatOpenid(String wechatOpenid) {
        this.wechatOpenid = wechatOpenid;
    }

    public String getPerCost() {
        return perCost;
    }

    public void setPerCost(String perCost) {
        this.perCost = perCost;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public String getSportImgUrl() {
        return sportImgUrl;
    }

    public void setSportImgUrl(String sportImgUrl) {
        this.sportImgUrl = sportImgUrl;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public String getNumType() {
        return numType;
    }

    public void setNumType(String numType) {
        this.numType = numType;
    }

    public String getCostRMB() {
        return costRMB;
    }

    public void setCostRMB(String costRMB) {
        this.costRMB = costRMB;
    }




    public Integer getTotalNumUp() {
        return totalNumUp;
    }

    public void setTotalNumUp(Integer totalNumUp) {
        this.totalNumUp = totalNumUp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public Integer getCurrNum() {
        return currNum;
    }

    public void setCurrNum(Integer currNum) {
        this.currNum = currNum;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public String getIsFull() {
        return isFull;
    }

    public void setIsFull(String isFull) {
        this.isFull = isFull == null ? null : isFull.trim();
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus == null ? null : gameStatus.trim();
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
    }

    public String getGameLocation() {
        return gameLocation;
    }

    public void setGameLocation(String gameLocation) {
        this.gameLocation = gameLocation == null ? null : gameLocation.trim();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    public String getActureStartTm() {
        return actureStartTm;
    }

    public void setActureStartTm(String actureStartTm) {
        this.actureStartTm = actureStartTm == null ? null : actureStartTm.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public String getSportTitle() {
        return sportTitle;
    }

    public void setSportTitle(String sportTitle) {
        this.sportTitle = sportTitle == null ? null : sportTitle.trim();
    }

    public BigDecimal getProjectCost() {
        return projectCost;
    }

    public void setProjectCost(BigDecimal projectCost) {
        this.projectCost = projectCost;
    }

    public String getFeeTags() {
        return feeTags;
    }

    public void setFeeTags(String feeTags) {
        this.feeTags = feeTags == null ? null : feeTags.trim();
    }

    public BigDecimal getActivityHour() {
        return activityHour;
    }

    public void setActivityHour(BigDecimal activityHour) {
        this.activityHour = activityHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getContactDir() {
        return contactDir;
    }

    public void setContactDir(String contactDir) {
        this.contactDir = contactDir == null ? null : contactDir.trim();
    }

    public String getStartWechatOpenid() {
        return startWechatOpenid;
    }

    public void setStartWechatOpenid(String startWechatOpenid) {
        this.startWechatOpenid = startWechatOpenid == null ? null : startWechatOpenid.trim();
    }
}