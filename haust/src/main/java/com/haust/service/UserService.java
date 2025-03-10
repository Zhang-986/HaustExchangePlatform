package com.haust.service;

import com.haust.domain.dto.AccountDTO;

public interface UserService {
    String loginByAdmin(AccountDTO accountDTO);
}
