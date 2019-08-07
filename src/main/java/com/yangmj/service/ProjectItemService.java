package com.yangmj.service;

import com.yangmj.common.MyPageInfo;
import com.yangmj.entity.ProjectItem;

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
}
