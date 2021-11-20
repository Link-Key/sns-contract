package com.peifeng.be.mapper;

import com.peifeng.be.model.Account;
import com.peifeng.be.model.AccountExample;
import java.util.List;

public interface AccountMapper {
    int deleteByExample(AccountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Account record);

    int insertSelective(Account record);

    List<Account> selectByExample(AccountExample example);

    Account selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    List<Account> selectAccounts();

    int accountCount();

    void batchUpdate(List<String> accountLists);
}