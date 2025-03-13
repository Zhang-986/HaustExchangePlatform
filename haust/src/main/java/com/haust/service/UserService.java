package com.haust.service;

import com.haust.domain.dto.AccountDTO;
import com.haust.domain.vo.RoleVo;
import com.haust.result.ResultResponse;

import javax.management.relation.Role;

public interface UserService {
    RoleVo loginByAdmin(AccountDTO accountDTO);

    void register(AccountDTO accountDTO);

    RoleVo loginByUser(AccountDTO accountDTO);
}
