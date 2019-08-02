package xin.yangmj.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//@Data
//@JsonIgnoreProperties(value={"status","updateTime"})
public abstract class BaseEntity {
    /**
     * 每页显示数  默认
     */
//    @JsonIgnore
    private int pageSize =10 ;
    /**
     * 第几页 默认
     */
//    @JsonIgnore
    private int pageNum =1;

    @JsonIgnore
    public int getPageSize() {
        return pageSize;
    }

    @JsonProperty
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @JsonIgnore
    public int getPageNum() {
        return pageNum;
    }

    @JsonProperty
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}