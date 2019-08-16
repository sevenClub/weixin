package com.yangmj.service;


import com.alibaba.fastjson.JSONObject;
import com.yangmj.entity.TemplateData;
import com.yangmj.entity.WxMssVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MessagePushService {



    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;


    /**
     *
     * @param accessToken  app的token
     * @param openid 用户openid
     * @param formId 表单ID
     * @param templateId 模板ID
     * @param keywords {与模板字段一一对应}
     * @return
     */
    public String pushOneUser(String accessToken,String openid, String formId,String templateId,String[] keywords) {

   /*     //如果access_token为空则从新获取
        if(StringUtils.isEmpty(accessToken)){
            accessToken = getAccess_token();
        }*/

        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" +
                "?access_token=" + accessToken;

        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(openid);//用户openid
        wxMssVo.setForm_id(formId);//formId
        wxMssVo.setTemplate_id(templateId);//模版id
        Map<String, TemplateData> m = new HashMap<>();

        //封装数据
        if(keywords.length>0){
            for(int i=1;i<=keywords.length;i++){
                TemplateData keyword = new TemplateData();
                keyword.setValue(keywords[i-1]);
                m.put("keyword"+i, keyword);
            }
            wxMssVo.setData(m);
        }else{
            log.error("keywords长度为空");
            return null;
        }

        if(restTemplate==null){
            restTemplate = new RestTemplate();
        }

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxMssVo, String.class);
        log.error("小程序推送结果={}", responseEntity.getBody());
        return responseEntity.getBody();
    }

    /*
     * 获取access_token
     * appid和appsecret到小程序后台获取，当然也可以让小程序开发人员给你传过来
     * */
    public String getAccess_token() {
        //获取access_token
        String appid = "wxb1abfc1724c5ee8b";
        String appsecret = "c88a5dd3c3af8228a389d778fce3e32b";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appid + "&secret=" + appsecret;
        if(restTemplate==null){
            restTemplate = new RestTemplate();
        }
        String json = restTemplate.getForObject(url, String.class);
        JSONObject myJson = JSONObject.parseObject(json);
        return myJson.get("access_token").toString();
    }

    public static void main(String[] args) {
        System.out.println(new MessagePushService().getAccess_token());

        MessagePushService weChatUtil = new MessagePushService();
        String values[] ={"Jack方","2019-5-8 10:10:10","xxx有限公司","JAVA开发","xx区xx广场xx号","请带好入职材料"};
        weChatUtil.pushOneUser(weChatUtil.getAccess_token()
                ,"o_fh25E0IufW7NIpezUReODfVH68","ec76b8b81cd04cf6b464bb0adf309d3b","zv0IsYDpJxgKWLHGUy8FEv0ajtJqkfhWTsFWiM7zzSU"
                ,values);
    }

}


