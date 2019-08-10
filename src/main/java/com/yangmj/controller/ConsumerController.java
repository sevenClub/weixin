package com.yangmj.controller;

import com.yangmj.common.SystemDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.yangmj.common.ResponseResult;
import com.yangmj.entity.Consumer;
import com.yangmj.service.ConsumerService;

@RestController
@Slf4j
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @PostMapping("/queryConsumerInfo")
    public ResponseResult queryConsumerInfo(@RequestBody Consumer consumer) {

        String wechatOpenid = consumer.getWechatOpenid();
        log.info("登录的用户是{}",wechatOpenid);
        ResponseResult resp = null;
        if (StringUtils.isEmpty(wechatOpenid)) {
            log.info("缺少wechatOpenid");
            return resp = ResponseResult.makeFailResponse(SystemDefault.PARAMETER_MISSING, "");
        }
        Consumer consumerQuery = consumerService.findConsumerByWechatOpenid(wechatOpenid);
        if (StringUtils.isEmpty(consumerQuery)) {
            return resp = ResponseResult.makeFailResponse(SystemDefault.SERVER_ERROR_500, "");
        }
        resp = ResponseResult.makeSuccResponse(null, consumerQuery);
        return resp;

    }
}
