package com.peifeng.be.mapper;

import com.peifeng.be.model.RobotAccount;
import com.peifeng.be.model.RobotAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RobotAccountMapper {
    int countByExample(RobotAccountExample example);

    int deleteByExample(RobotAccountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RobotAccount record);

    int insertSelective(RobotAccount record);

    List<RobotAccount> selectByExample(RobotAccountExample example);

    RobotAccount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RobotAccount record, @Param("example") RobotAccountExample example);

    int updateByExample(@Param("record") RobotAccount record, @Param("example") RobotAccountExample example);

    int updateByPrimaryKeySelective(RobotAccount record);

    int robotAccountCount();

    void batchInsert(List<RobotAccount> robotAccounts);

    void batchDelete(List<Integer> ids);

    void batchUpdate(List<String> accountLists);
}