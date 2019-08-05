package xin.yangmj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xin.yangmj.common.ResponseResult;
import xin.yangmj.dto.AccountDto;
import xin.yangmj.dto.WechatAuthenticationResponse;
import xin.yangmj.entity.Consumer;
import xin.yangmj.service.WechatService;

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
            resp = ResponseResult.makeFailResponse("miss code/invalid code", "");
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resp;
    }

    @PostMapping("/updateConsumerInfo")
    public void updateConsumerInfo(@RequestBody Consumer consumer) {
        wechatService.updateConsumerInfo(consumer);
    }

}
