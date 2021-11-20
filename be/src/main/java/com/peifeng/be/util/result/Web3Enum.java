package com.peifeng.be.util.result;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 15:48
 */
public enum Web3Enum {
    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(200, "成功"),
    ERROR(500,"发送交易失败"),

    ;

    private Integer code;

    private String msg;

    Web3Enum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
