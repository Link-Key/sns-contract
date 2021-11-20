package com.peifeng.be.service.impl;

import com.peifeng.be.mapper.AccountMapper;
import com.peifeng.be.model.Account;
import com.peifeng.be.model.AccountExample;
import com.peifeng.be.service.AccountService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 10:30
 */
@Service(value = "accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public int addAccount(Account account) {
        if(account.getCreatetime()==null){
            account.setCreatetime(new Date());
        }
        account.setLssued(Boolean.FALSE);
        int insert = accountMapper.insert(account);
        return insert;
    }

    @Override
    public PageInfo<Account> findAllAccount(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Account> userDomains = accountMapper.selectAccounts();
        PageInfo result = new PageInfo(userDomains);
        return result;
    }

    @Override
    public int accountCount() {
        return accountMapper.accountCount();
    }

    @Override
    public List<Account> selectByExample(AccountExample accountExample) {
        return accountMapper.selectByExample(accountExample);
    }

    @Override
    public void batchUpdate(List<String> accountLists) {
        accountMapper.batchUpdate(accountLists);
    }


}
