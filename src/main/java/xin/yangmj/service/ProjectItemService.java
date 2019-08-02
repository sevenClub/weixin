package xin.yangmj.service;

import xin.yangmj.common.MyPageInfo;
import xin.yangmj.entity.ProjectItem;

public interface ProjectItemService {


    MyPageInfo<ProjectItem> queryProjectItemList(ProjectItem projectItem);

    /**
     * 新增的项目信息
     * @param projectItem
     * @return
     */
    int insertProjectItem(ProjectItem projectItem);

    int updateProjectItem(ProjectItem record);
}
