package com.haust.service;

import com.haust.domain.dto.AccountDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.vo.PageVO;
import com.haust.domain.vo.RoleVo;

import com.haust.mq.msg.UserMsg;

public interface UserService {
    RoleVo loginByAdmin(AccountDTO accountDTO);

    void register(AccountDTO accountDTO);

    RoleVo loginByUser(AccountDTO accountDTO);

    void addMonitor(UserMsg userMsg);

    PageVO<UserMsg> getMonitorLog(PageDTO pageDTO);

}
