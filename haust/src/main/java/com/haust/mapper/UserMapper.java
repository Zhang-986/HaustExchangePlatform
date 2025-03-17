package com.haust.mapper;

import com.haust.domain.dto.AccountDTO;
import com.haust.domain.po.User;
import com.haust.mq.msg.UserMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectById(AccountDTO accountDTO);

    void insert(User user);

    void solveTimes(UserMsg userMsg);

    List<UserMsg> pageByMonitor(Integer orderBy);
}
