package com.peifeng.be.service;

import com.github.pagehelper.PageInfo;
import com.peifeng.be.model.Account;
import com.peifeng.be.model.Contract;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author peifeng
 * @Date 2021/5/24 10:30
 */
public interface ContractService {

    int addContract(Contract account);

    Contract findAllContract();

    int updateByPrimaryKeySelective(Contract account);
}
