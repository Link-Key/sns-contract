package com.peifeng.be.handle;

import com.peifeng.be.util.exception.AccountException;
import com.peifeng.be.util.exception.AddressException;
import com.peifeng.be.util.result.Result;
import com.peifeng.be.util.result.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 15:52
 */
@ControllerAdvice
public class ExceptionHandle {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof AccountException) {   //判断异常是否是我们定义的异常
            AccountException accountException = (AccountException) e;
            return ResultUtils.error(accountException.getCode(), accountException.getMessage());
        }else if(e instanceof AddressException){
            AddressException addressException = (AddressException) e;
            return ResultUtils.error(addressException.getCode(), addressException.getMessage());
        }else {
            logger.error("【系统异常】{}", e);
            return ResultUtils.error(-1, "未知错误");
        }
    }
}
