package com.yangmj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.yangmj.common.MyPageInfo;
import com.yangmj.common.ResponseResult;
import com.yangmj.entity.ProjectItem;
import com.yangmj.service.ProjectItemService;

@RestController
public class ProjectController {


    @Autowired
    ProjectItemService projectItemService;

    /**
     * 根据主键id获取项目的信息
     * @param projectItem
     * @return
     */
    @PostMapping("/findAllProject")
    public ResponseResult queryProjectItem(@RequestBody ProjectItem projectItem){
        MyPageInfo<ProjectItem> projectItemPageInfo = projectItemService.queryProjectItemList(projectItem);
        ResponseResult resp = ResponseResult.makeSuccResponse(null, projectItemPageInfo);
        return resp;
    }

    /**
     * 创建项目的信息
     * @param projectItem
     * @return
     */
    @PostMapping("/createProjectItem")
    public ResponseResult createProjectItem(@RequestBody ProjectItem projectItem) {

        ResponseResult resp = null;
        try {
            int item = projectItemService.insertProjectItem(projectItem);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse(null, "失败");
        }
        return resp;
    }

    /**
     * 根据id更新活动名称
     * @param projectItem
     * @return
     */
    @PostMapping("/updateProjectItem")
    public ResponseResult updateProjectItem(@RequestBody ProjectItem projectItem) {

        ResponseResult resp = null;
        ProjectItem queryProject = projectItemService.queryProjectByKey(projectItem);
        if (null == queryProject) {
            return resp = ResponseResult.makeFailResponse("该项目已经不存在，请确认", "");
        }
        queryProject.setProjectName(projectItem.getProjectName());
        queryProject.setTotalNum(projectItem.getTotalNum());
        queryProject.setProjectCost(projectItem.getProjectCost());
        try {
            int item = projectItemService.updateByPrimaryKeySelective(queryProject);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse(null, "失败");
        }
        return resp;
    }
}
