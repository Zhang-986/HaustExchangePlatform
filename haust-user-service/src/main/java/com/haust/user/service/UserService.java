package com.haust.user.service;

import com.haust.common.domain.dto.AccountDTO;
import com.haust.common.domain.dto.PageDTO;
import com.haust.common.domain.vo.PageVO;
import com.haust.common.domain.vo.RoleVo;

import com.haust.mq.msg.UserMsg;

public interface UserService {
    RoleVo loginByAdmin(AccountDTO accountDTO);

    void register(AccountDTO accountDTO);

    RoleVo loginByUser(AccountDTO accountDTO);

    void addMonitor(UserMsg userMsg);

    PageVO<UserMsg> getMonitorLog(PageDTO pageDTO);

    String info(String text);
}
