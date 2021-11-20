package com.peifeng.be.util.exception;

import com.peifeng.be.util.result.AddressEnum;
import com.peifeng.be.util.result.ResultEnum;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 15:50
 */
public class AddressException extends RuntimeException{
    private Integer code;

    public AddressException(AddressEnum addressEnum) {
        super(addressEnum.getMsg());
        this.code = addressEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
