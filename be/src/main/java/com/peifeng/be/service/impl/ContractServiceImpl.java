package com.peifeng.be.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peifeng.be.mapper.AccountMapper;
import com.peifeng.be.mapper.ContractMapper;
import com.peifeng.be.model.Account;
import com.peifeng.be.model.Contract;
import com.peifeng.be.service.AccountService;
import com.peifeng.be.service.ContractService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 10:30
 */
@Service(value = "contractService")
public class ContractServiceImpl implements ContractService {

    @Resource
    private ContractMapper contractMapper;

    @Override
    public int addContract(Contract account) {
        int insert = contractMapper.insert(account);
        return insert;
    }

    @Override
    public Contract findAllContract() {
        return contractMapper.selectContract();
    }

    @Override
    public int updateByPrimaryKeySelective(Contract account) {
        return contractMapper.updateByPrimaryKeySelective(account);
    }
}
