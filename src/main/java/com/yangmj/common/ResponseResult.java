package com.yangmj.common;

import org.springframework.util.StringUtils;

public class ResponseResult {
    private String code;

    private String msg;

    private Object data;

    public static ResponseResult makeSuccResponse(String msg, Object data) {

        ResponseResult  resp = new ResponseResult();
        resp.setCode(ResponseEnum.SUCC.getCode());
        resp.setData(data);

        if (StringUtils.isEmpty(msg)) {
            resp.setMsg(ResponseEnum.SUCC.getMsg());
        } else {
            resp.setMsg(msg);
        }

        return resp;
    }

    public static ResponseResult makeFailResponse(String msg, Object data) {
        ResponseResult  resp = new ResponseResult();
        resp.setCode(ResponseEnum.FAIL.getCode());
        resp.setData(data);

        if (StringUtils.isEmpty(msg)) {
            resp.setMsg(ResponseEnum.FAIL.getMsg());
        } else {
            resp.setMsg(msg);
        }

        return resp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
