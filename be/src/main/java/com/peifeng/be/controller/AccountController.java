package com.peifeng.be.controller;

import com.peifeng.be.model.Account;
import com.peifeng.be.model.AccountExample;
import com.peifeng.be.service.AccountService;
import com.peifeng.be.util.IPUtils;
import com.peifeng.be.util.Web3JUtils;
import com.peifeng.be.util.exception.AddressException;
import com.peifeng.be.util.result.AddressEnum;
import com.peifeng.be.util.result.Result;
import com.peifeng.be.util.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.peifeng.be.service.AccountService;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 10:28
 */
@Controller
@RequestMapping(value = "/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @ResponseBody
    @PostMapping("/add")
    public Result<Account> addAccount(@RequestBody  Account account){

        if(!Web3JUtils.isETHValidAddress(account.getAddress())){
            throw new AddressException(AddressEnum.FORMAT_ERROR);
        }
        Result result = new Result();
        try {
            int num =  accountService.addAccount(account);
            if(num == 1) {
                result.setCode(ResultEnum.SUCCESS.getCode());
                result.setMsg(ResultEnum.SUCCESS.getMsg());
                result.setData(num);
            }
        }catch (Exception duplicateKeyException){
            result.setCode(ResultEnum.REPEAT.getCode());
            result.setMsg(ResultEnum.REPEAT.getMsg());
        }


        return result;
    }

    //@ResponseBody
    //@GetMapping("/ip")
    //public void ip(HttpServletRequest request){
    //    String ipAddress = IPUtils.getIpAddr(request);
    //    System.out.println(ipAddress);
    //}

    @ResponseBody
    @GetMapping("/all")
    public Object findAllUser(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                    int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                    int pageSize){
        return accountService.findAllAccount(pageNum,pageSize);
    }

    @ResponseBody
    @PostMapping("/find")
    public List<Account> findAllUser(@RequestBody(required = false) Account account){
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria criteria = accountExample.createCriteria();
        if(account != null &&  !StringUtils.isEmpty(account.getAddress())){
            criteria.andAddressLike("%" + account.getAddress() + "%");
        }
        if(account != null && account.getLssued()!= null){
            criteria.andLssuedEqualTo(account.getLssued());
        }
        return accountService.selectByExample(accountExample);
    }
}




