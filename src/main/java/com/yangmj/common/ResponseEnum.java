package com.yangmj.common;

public enum ResponseEnum {
    SUCC("00", "成功"),
    FAIL("01", "失败");

    private final String code;

    private final String msg;

    /**
     * @param code  返回码
     * @param msg   返回消息
     */
    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
