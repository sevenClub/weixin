package com.yangmj.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.yangmj.entity.ProjectItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectItemMapper {
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增的项目信息
     * @param projectItem
     * @return
     */
    int insertProjectItem(ProjectItem projectItem);

    int insertSelective(ProjectItem projectItem);

    /**
     * 查询所有的项目信息
     * @param projectItem
     * @return
     */
    List<ProjectItem> queryProjectItemList(ProjectItem projectItem);

    int updateByPrimaryKeySelective(ProjectItem record);

    int updateProjectItem(ProjectItem record);
    ProjectItem queryProjectByKey(ProjectItem record);

    /**
     * 查询所有项目的url地址
     * 详情页面大图
     * sportImgUrl
     * 订单首页小图
     * firstPageUrl
     * 目前没有用到，后期可以优化使用
     */

    @Select("select item.project_id projectId ,project.sport_img_url sportImgUrl ,project.first_page_url firstPageUrl from order_item item ,project_item project where item.project_id = project.project_id ")
    List<Map> queryAllProjectUrl();

}