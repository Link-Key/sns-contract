package com.peifeng.be.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peifeng.be.mapper.AccountMapper;
import com.peifeng.be.mapper.RobotAccountMapper;
import com.peifeng.be.model.Account;
import com.peifeng.be.model.RobotAccount;
import com.peifeng.be.model.RobotAccountExample;
import com.peifeng.be.service.RobotAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/6/7 17:54
 */
@Service(value = "robotAccountService")
public class RobotAccountServiceImp implements RobotAccountService {
    @Resource
    private RobotAccountMapper robotAccountMapper;

    @Override
    public int addRobotAccount(RobotAccount robotAccount) {
        return robotAccountMapper.insert(robotAccount);
    }

    @Override
    public PageInfo<RobotAccount> findAllRobotAccount(int pageNum, int pageSize,RobotAccountExample robotAccountExample) {
        PageHelper.startPage(pageNum, pageSize);
        List<RobotAccount> userDomains = robotAccountMapper.selectByExample(robotAccountExample);
        PageInfo result = new PageInfo(userDomains);
        return result;
    }

    @Override
    public void batchInsert(List<RobotAccount> robotAccounts){
        robotAccountMapper.batchInsert(robotAccounts);
    }

    @Override
    public List<RobotAccount> selectByExample(RobotAccountExample robotAccountExample) {
        return robotAccountMapper.selectByExample(robotAccountExample);
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        robotAccountMapper.batchDelete(ids);
    }

    @Override
    public int robotAccountCount() {
        return robotAccountMapper.robotAccountCount();
    }

    @Override
    public void batchUpdate(List<String> accountLists) {
        robotAccountMapper.batchUpdate(accountLists);
    }
}
