package xin.yangmj.mapper;

import org.apache.ibatis.annotations.Mapper;
import xin.yangmj.entity.ProjectItem;

import java.util.List;

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


}