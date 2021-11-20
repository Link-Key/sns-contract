package com.peifeng.be.service;

import com.github.pagehelper.PageInfo;
import com.peifeng.be.model.Account;
import com.peifeng.be.model.RobotAccount;
import com.peifeng.be.model.RobotAccountExample;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/6/7 17:54
 */
public interface RobotAccountService {
    int addRobotAccount(RobotAccount robotAccount);

    PageInfo<RobotAccount> findAllRobotAccount(int pageNum, int pageSize, RobotAccountExample robotAccountExample);

    int robotAccountCount();

    void batchInsert(List<RobotAccount> robotAccounts);

    List<RobotAccount> selectByExample(RobotAccountExample robotAccountExample);

    void batchDelete(List<Integer> ids);

    void batchUpdate(List<String> accountLists);
}
