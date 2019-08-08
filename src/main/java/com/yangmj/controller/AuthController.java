package com.yangmj.controller;

import com.yangmj.common.SystemDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.yangmj.common.ResponseResult;
import com.yangmj.dto.AccountDto;
import com.yangmj.dto.WechatAuthenticationResponse;
import com.yangmj.entity.Consumer;
import com.yangmj.service.WechatService;

@RestController
@Slf4j
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WechatService wechatService;

    @GetMapping("/test")
    public String test() {
        return "test_success";
    }

    @GetMapping("/testAuth")
    public String testAuth() {
        return "testAuth_success";
    }

    @PostMapping("/auth")
    public ResponseResult createAuthenticationToken(@RequestBody AccountDto accountDto)
            throws AuthenticationException {
        ResponseResult resp = null;
        try {
            WechatAuthenticationResponse jwtResponse = wechatService.wechatLogin(accountDto.getCode());
            resp = ResponseResult.makeSuccResponse(null, jwtResponse);
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse(SystemDefault.PARAMETER_MISSING+"miss code/invalid code", "");
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resp;
    }

    @PostMapping("/updateConsumerInfo")
    public ResponseResult updateConsumerInfo(@RequestBody Consumer consumer) {
        ResponseResult resp = null;
        String consumerInfoResult = wechatService.updateConsumerInfo(consumer);

        if ("00".equals(consumerInfoResult)) {
            resp = ResponseResult.makeSuccResponse(null, consumerInfoResult);
        } else {
            resp = ResponseResult.makeFailResponse(SystemDefault.NETWORK_ERROR, "");
        }
        return resp;
    }

}
