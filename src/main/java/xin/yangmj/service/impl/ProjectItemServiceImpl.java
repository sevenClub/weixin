package xin.yangmj.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xin.yangmj.common.MyPageInfo;
import xin.yangmj.entity.ProjectItem;
import xin.yangmj.mapper.ProjectItemMapper;
import xin.yangmj.service.ProjectItemService;
import xin.yangmj.util.DateUtil;
import xin.yangmj.util.RandomUtil;

import java.util.List;

@Service
public class ProjectItemServiceImpl implements ProjectItemService {

    @Autowired
    ProjectItemMapper projectItemMapper;


    @Override
    public MyPageInfo<ProjectItem> queryProjectItemList(ProjectItem projectItem) {
        PageHelper.startPage(projectItem.getPageNum(),projectItem.getPageSize());
        List<ProjectItem> projectItemList = projectItemMapper.queryProjectItemList(projectItem);
        MyPageInfo<ProjectItem> projectItemPageInfo = new MyPageInfo<>(projectItemList);
        return projectItemPageInfo;
    }

    @Override
    public int insertProjectItem(ProjectItem projectItem) {

        //生产随机的projectid
        String project_id = RandomUtil.generateString(8);
        String tmpId = projectItem.getProjectId();
//        其他运动的时候是99 会生成随机的数字
        if (!StringUtils.isEmpty(tmpId) && "99".equals(tmpId)) {
            projectItem.setProjectId(project_id);
        }
        int item = projectItemMapper.insertProjectItem(projectItem);
        return item;
    }

    @Override
    public int updateByPrimaryKeySelective(ProjectItem projectItem) {

        //更新时间
        String updateTime = DateUtil.formatDateTime();
        projectItem.setUpdateTime(updateTime);
        return projectItemMapper.updateProjectItem(projectItem);
    }

    @Override
    public ProjectItem queryProjectByKey(ProjectItem record) {
        return projectItemMapper.queryProjectByKey(record);
    }
}
