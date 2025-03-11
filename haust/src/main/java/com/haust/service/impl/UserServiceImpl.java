package com.haust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.haust.constant.MessageConstant;
import com.haust.constant.UserConstant;
import com.haust.domain.dto.AccountDTO;
import com.haust.domain.po.User;
import com.haust.exception.MessageException;
import com.haust.exception.UserException;
import com.haust.mapper.UserMapper;
import com.haust.service.UserService;
;import com.haust.util.JwtUtil;
import com.haust.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    @Override
    public String loginByAdmin(AccountDTO accountDTO) {
        // 0.判断当前DTO是否为null
        if(BeanUtil.isEmpty(accountDTO)){
            throw new UserException(UserConstant.LOGIN_EMPTY);
        }

        // 1.查看账号数据库是否存在
        User user = userMapper.selectById(accountDTO);
        if(BeanUtil.isEmpty(user)){
            throw  new UserException(UserConstant.LOGIN_CHECK_EMPTY);
        }
        // 2. 判断传入密码与加密后的密码
        if(!PasswordUtil.matches(accountDTO.getPassword(), user.getPassword())){
            throw new UserException(UserConstant.PASSWORD_WRONG);
        }
        // 2.5 判断当前账户状态
        if(user.getRole()==2){
            throw new UserException(UserConstant.USER_TROUBLE);
        }
        // 3.生成JWT令牌
        String jwt = JwtUtil.generateToken(String.valueOf(user.getId()));

        return jwt;
    }
}
