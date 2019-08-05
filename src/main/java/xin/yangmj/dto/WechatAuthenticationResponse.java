package xin.yangmj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WechatAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    @JsonProperty("access_token")
    private final String accessToken;
    @JsonProperty("wx_OpenId")
    private final String wxOpenId;

    @JsonProperty("code")
    private final String code;

    public String getWxOpenId() {
        return wxOpenId;
    }

    public WechatAuthenticationResponse(String accessToken, String wxOpenId,String code) {
        this.accessToken = accessToken;
        this.wxOpenId = wxOpenId;
        this.code = code;
    }

    public String getAccess_token() {
        return this.accessToken;
    }

}
