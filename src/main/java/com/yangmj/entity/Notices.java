package com.yangmj.entity;

public class Notices {

    //活动名称 团队名称
    private String sportTitle;
    //活动时间 比赛时间
    private String sportTimeToEnd;
    //活动地点 比赛地点
    private String sportLocation;
    //参与人数
    private String sportNum;
    //场次时间
    private String leverTime;
    //地址信息
    private String sportAddr;

    //加入时间
    private String partInTime;
    //团长昵称
    private String captionNickName;
    //联系方式
    private String captionPhone;
    //发起时间
    private String sportCreateTime;

    public String getSportTitle() {
        return sportTitle;
    }

    public void setSportTitle(String sportTitle) {
        this.sportTitle = sportTitle;
    }

    public String getSportTimeToEnd() {
        return sportTimeToEnd;
    }

    public void setSportTimeToEnd(String sportTimeToEnd) {
        this.sportTimeToEnd = sportTimeToEnd;
    }

    public String getSportLocation() {
        return sportLocation;
    }

    public void setSportLocation(String sportLocation) {
        this.sportLocation = sportLocation;
    }

    public String getSportNum() {
        return sportNum;
    }

    public void setSportNum(String sportNum) {
        this.sportNum = sportNum;
    }

    public String getLeverTime() {
        return leverTime;
    }

    public void setLeverTime(String leverTime) {
        this.leverTime = leverTime;
    }

    public String getSportAddr() {
        return sportAddr;
    }

    public void setSportAddr(String sportAddr) {
        this.sportAddr = sportAddr;
    }

    public String getPartInTime() {
        return partInTime;
    }

    public void setPartInTime(String partInTime) {
        this.partInTime = partInTime;
    }

    public String getCaptionNickName() {
        return captionNickName;
    }

    public void setCaptionNickName(String captionNickName) {
        this.captionNickName = captionNickName;
    }

    public String getCaptionPhone() {
        return captionPhone;
    }

    public void setCaptionPhone(String captionPhone) {
        this.captionPhone = captionPhone;
    }

    public String getSportCreateTime() {
        return sportCreateTime;
    }

    public void setSportCreateTime(String sportCreateTime) {
        this.sportCreateTime = sportCreateTime;
    }
}
