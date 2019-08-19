package com.yangmj.util;

import com.yangmj.config.WechatAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class CommonUtils {

    @Value("${login.pageUrl}")
    private  String loginPageUrl;


    @Autowired
    WechatAuthProperties wechatAuthProperties;

    private RestTemplate wxAuthRestTemplate = new RestTemplate();

    public static String checkPhone(String phone) {
        String resp = "00";
        if (StringUtils.isEmpty(phone)) {
            return resp = "01";
        }
        if (!StringUtils.isEmpty(phone)) {
            boolean mobileResult = isMobile(phone);
            if (!mobileResult) {
                return resp = "02";
            }
        }
        return resp;
    }

    /**
     * 验证手机号
     * @param str
     * @return
     */
    public static boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }



}
