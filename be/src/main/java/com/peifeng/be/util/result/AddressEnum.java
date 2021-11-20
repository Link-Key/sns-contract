package com.peifeng.be.util.result;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 15:48
 */
public enum AddressEnum {
    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(200, "成功"),
    FORMAT_ERROR(300,"地址格式错误"),
    PRIVATEKEY_ERROR(400,"私钥格式错误")

    ;

    private Integer code;

    private String msg;

    AddressEnum(Integer code, String msg) {
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
