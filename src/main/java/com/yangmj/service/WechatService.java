package com.yangmj.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.yangmj.config.WechatAuthProperties;
import com.yangmj.dto.WechatAuthCodeResponse;
import com.yangmj.dto.WechatAuthenticationResponse;
import com.yangmj.entity.Consumer;
import com.yangmj.mapper.ConsumerMapper;
import com.yangmj.util.DateUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class WechatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatService.class);

    @Autowired
    private ConsumerMapper consumerMapper;

    @Value("${auth.wechat.appId}")
    private String appId;

    @Value("${auth.wechat.secret}")
    private String secret;

    /**
     * 服务器第三方session有效时间，单位秒, 默认1天
     */
    private static final Long EXPIRES = 86400L;

    private RestTemplate wxAuthRestTemplate = new RestTemplate();

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public WechatAuthenticationResponse wechatLogin(String code) {
        WechatAuthCodeResponse response = getWxSession(code);

        String wxOpenId = response.getOpenid();
        String wxSessionKey = response.getSessionKey();
        Consumer consumer = new Consumer();
        consumer.setWechatOpenid(wxOpenId);
        loginOrRegisterConsumer(consumer);

        Long expires = response.getExpiresIn();
        String thirdSession = create3rdSession(wxOpenId, wxSessionKey, expires);
        return new WechatAuthenticationResponse(thirdSession,wxOpenId,code);
    }

    public WechatAuthCodeResponse getWxSession(String code) {
        LOGGER.info(code);
        String urlString = "?appid={appid}&secret={srcret}&js_code={code}&grant_type={grantType}";
        String response = wxAuthRestTemplate.getForObject(
                wechatAuthProperties.getSessionHost() + urlString, String.class,
                wechatAuthProperties.getAppId(),
                wechatAuthProperties.getSecret(),
                code,
                wechatAuthProperties.getGrantType());
//        String response = "{\"session_key\":\"f7wSl5kQDn+jobA4Dl3FFA==\",\"openid\":\"oa00R5VnS0hB_7XJTaAsD8Egx2I8\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerFor(WechatAuthCodeResponse.class);
        WechatAuthCodeResponse res;
        try {
            res = reader.readValue(response);
        } catch (IOException e) {
            res = null;
            LOGGER.error("反序列化失败", e);
        }
        LOGGER.info(response);
        if (null == res) {
            throw new RuntimeException("调用微信接口失败");
        }
        if (res.getErrcode() != null) {
            throw new RuntimeException(res.getErrmsg());
        }
        res.setExpiresIn(res.getExpiresIn() != null ? res.getExpiresIn() : EXPIRES);
        return res;
    }

    public String create3rdSession(String wxOpenId, String wxSessionKey, Long expires) {
        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        StringBuffer sb = new StringBuffer();
        sb.append(wxSessionKey).append("#").append(wxOpenId);

        stringRedisTemplate.opsForValue().set(thirdSessionKey, sb.toString(), expires, TimeUnit.SECONDS);
        return thirdSessionKey;
    }

    private void loginOrRegisterConsumer(Consumer consumer) {
        Consumer consumer1 = consumerMapper.findConsumerByWechatOpenid(consumer.getWechatOpenid());
        if (null == consumer1) {
            consumerMapper.insertConsumer(consumer);
        }
    }

    /**
     * 更新用户的信息
     * @param consumer
     * @return
     */
    public String updateConsumerInfo(Consumer consumer) {
        String result = "00";
        try {
//            Consumer consumerExist = consumerMapper.findConsumerByWechatOpenid(AppContext.getCurrentUserWechatOpenId());
            Consumer consumerExist = consumerMapper.findConsumerByWechatOpenid(consumer.getWechatOpenid());
            if (null != consumerExist && !"".equals(consumerExist)) {
                consumerExist.setUpdatedBy(1L);
                consumerExist.setUpdatedAt(DateUtil.formatDateTime());
                consumerExist.setGender(consumer.getGender());
                consumerExist.setAvatarUrl(consumer.getAvatarUrl());
                consumerExist.setWechatOpenid(consumer.getWechatOpenid());
                consumerExist.setEmail(consumer.getEmail());
                consumerExist.setNickName(consumer.getNickName());
                consumerExist.setPhone(consumer.getPhone());
                consumerExist.setUsername(consumer.getUsername());
                consumerMapper.updateConsumer(consumerExist);
            } else {
                result = "02";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "01";
        }
        return result;
    }

    public String loginWechatNotice() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ this.appId +"&secret="+ this.secret +"";
        System.out.println("url=====+微信消息通知"+url);
        String response = wxAuthRestTemplate
                .getForObject(url,String.class);
        JSONObject object = (JSONObject) JSONObject.parse(response);
        String access_token = (String)object.get("access_token");
        return access_token;
    }

}
