package com.peifeng.be.controller;

import com.github.pagehelper.PageInfo;
import com.peifeng.be.model.Account;
import com.peifeng.be.model.AccountExample;
import com.peifeng.be.model.RobotAccount;
import com.peifeng.be.model.RobotAccountExample;
import com.peifeng.be.service.RobotAccountService;
import com.peifeng.be.util.result.Result;
import com.peifeng.be.util.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/6/10 15:40
 */
@Controller
@RequestMapping(value = "/api/robotAccount")
public class RobotAccountController {

    @Autowired
    RobotAccountService robotAccountService;

    @ResponseBody
    @PostMapping("/find")
    public PageInfo<RobotAccount> findAllUser(@RequestBody Map<String, String> params){
        Integer pageNum = (Integer.parseInt(params.get("pageNum")));
        Integer pageSize = (Integer.parseInt(params.get("pageSize")));
        String address = params.get("robotAccount");
        RobotAccountExample robotAccountExample = new RobotAccountExample();
        RobotAccountExample.Criteria criteria = robotAccountExample.createCriteria();
        if(!StringUtils.isEmpty(address)){
            criteria.andAddressLike("%" + address + "%");
        }

        return robotAccountService.findAllRobotAccount(pageNum,pageSize,robotAccountExample);
    }

    @ResponseBody
    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, List<Integer>> params){
        List<Integer> ids = params.get("ids");
        Result result = new Result();

        try{
            robotAccountService.batchDelete(ids);
            result.setMsg(ResultEnum.SUCCESS.getMsg());
            result.setCode(ResultEnum.SUCCESS.getCode());
        }catch (Exception exception){
            result.setMsg(ResultEnum.UNKONW_ERROR.getMsg());
            result.setCode(ResultEnum.UNKONW_ERROR.getCode());
        }
        return result;
    }
}
