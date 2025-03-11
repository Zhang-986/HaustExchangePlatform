package com.haust.mapper;

import com.haust.domain.dto.AccountDTO;
import com.haust.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper {

    User selectById(AccountDTO accountDTO);

    void insert(User user);
}
