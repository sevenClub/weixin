package com.yangmj.service;

import com.yangmj.common.MyPageInfo;
import com.yangmj.entity.ProjectItem;

import java.util.List;
import java.util.Map;

public interface ProjectItemService {


    MyPageInfo<ProjectItem> queryProjectItemList(ProjectItem projectItem);

    /**
     * 新增的项目信息
     * @param projectItem
     * @return
     */
    int insertProjectItem(ProjectItem projectItem);

    int updateByPrimaryKeySelective(ProjectItem record);

    ProjectItem queryProjectByKey(ProjectItem record);
    /**
     * 查询所有项目的url地址
     * 详情页面大图
     * sportImgUrl
     * 订单首页小图
     * firstPageUrl
     */

    List<Map> queryAllProjectUrl();
}
