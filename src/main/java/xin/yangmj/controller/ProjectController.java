package xin.yangmj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xin.yangmj.common.MyPageInfo;
import xin.yangmj.common.ResponseResult;
import xin.yangmj.entity.ProjectItem;
import xin.yangmj.service.ProjectItemService;

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
//    public ProjectItem queryProjectItem(@RequestBody JSONObject jsonObject){
//        String a=jsonObject.get("id").toString();
//        Integer id=Integer.parseInt(a);
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
        try {
            int item = projectItemService.updateProjectItem(projectItem);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse(null, "失败");
        }
        return resp;
    }
}
