package xin.yangmj.entity;

import xin.yangmj.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetails extends BaseEntity {
    private Integer id;

    private String wechatOpenid;

    private String isActive;

    private Date createTime;

    private String updateTime;

    private BigDecimal projectFee;

    private Integer orderId;

    private String contactDir;

    private String isCaptain;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public void setWechatOpenid(String wechatOpenid) {
        this.wechatOpenid = wechatOpenid == null ? null : wechatOpenid.trim();
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive == null ? null : isActive.trim();
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

    public BigDecimal getProjectFee() {
        return projectFee;
    }

    public void setProjectFee(BigDecimal projectFee) {
        this.projectFee = projectFee;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getContactDir() {
        return contactDir;
    }

    public void setContactDir(String contactDir) {
        this.contactDir = contactDir == null ? null : contactDir.trim();
    }

    public String getIsCaptain() {
        return isCaptain;
    }

    public void setIsCaptain(String isCaptain) {
        this.isCaptain = isCaptain == null ? null : isCaptain.trim();
    }
}