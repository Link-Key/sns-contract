package com.peifeng.be.mapper;

import com.peifeng.be.model.Contract;

public interface ContractMapper {
    int deleteByPrimaryKey(String address);

    int insert(Contract record);

    int insertSelective(Contract record);

    Contract selectContract();

    int updateByPrimaryKeySelective(Contract contract);
}