package com.peifeng.be.service;

import com.github.pagehelper.PageInfo;
import com.peifeng.be.model.Account;
import com.peifeng.be.model.AccountExample;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 10:30
 */

public interface AccountService {

    int addAccount(Account account);

    PageInfo<Account> findAllAccount(int pageNum, int pageSize);

    int accountCount();

    List<Account> selectByExample(AccountExample accountExample);

    void batchUpdate(List<String> accountLists);
}
