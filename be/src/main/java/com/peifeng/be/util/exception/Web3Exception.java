package com.peifeng.be.util.exception;

import com.peifeng.be.util.result.ResultEnum;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 15:50
 */
public class Web3Exception extends RuntimeException{
    private Integer code;

    public Web3Exception(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
