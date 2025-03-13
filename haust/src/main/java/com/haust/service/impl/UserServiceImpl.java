package com.haust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.haust.constant.RedisConstant;
import com.haust.domain.dto.AccountDTO;
import com.haust.domain.po.User;
import com.haust.domain.vo.RoleVo;
import com.haust.exception.BusinessException;
import com.haust.mapper.UserMapper;
import com.haust.result.ResultResponse;
import com.haust.service.UserService;
;import com.haust.util.JwtUtil;
import com.haust.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final StringRedisTemplate redisTemplate;
    private final UserMapper userMapper;
    @Override
    public RoleVo loginByAdmin(AccountDTO accountDTO) {
        // 0.判断当前DTO是否为null
        if (BeanUtil.isEmpty(accountDTO)) {
            throw new BusinessException("LOGIN_EMPTY", "AccountDTO cannot be empty");
        }
        // 1.查看账号数据库是否存在
        User user = userMapper.selectById(accountDTO);
        if(BeanUtil.isEmpty(user)){
            throw new BusinessException("NO_ACCOUNT","The current account is null");
        }
        // 2. 判断传入密码与加密后的密码
        if(!PasswordUtil.matches(accountDTO.getPassword(), user.getPassword())){
            throw new BusinessException("PASSWORD_WRONG","The password is wrong");
        }
        // 2.5 判断当前账户状态
        if(user.getRole()==2){
            throw new BusinessException("ACCOUNT_WRONG","The account is trouble");
        }
        // 3.生成JWT令牌
        String jwt = JwtUtil.generateToken(String.valueOf(user.getId()));
        RoleVo roleVo = new RoleVo();
        roleVo.setRole(user.getRole());
        roleVo.setToken(jwt);
        return roleVo;
    }

    @Override
    public void register(AccountDTO accountDTO) {
        // 0 当前参数为null
        if (BeanUtil.isEmpty(accountDTO)) {
            throw new BusinessException("LOGIN_EMPTY", "AccountDTO cannot be empty");
        }
        // 1. 对密码进行加密
        String pwd = PasswordUtil.encryptPassword(accountDTO.getPassword());
        // 2. 构建用户对象
        User user = User.builder()
                .account(accountDTO.getAccount())
                .password(pwd).build();
        // 3. 插入数据库
        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("USER_ALREADY_EXISTS", "Account already exists");
        }

    }

    @Override
    public RoleVo loginByUser(AccountDTO accountDTO) {
        // 1. 判断当前DTO是否有效
        if (BeanUtil.isEmpty(accountDTO)) {
            throw new BusinessException("LOGIN_EMPTY", "AccountDTO cannot be empty");
        }
        // 2.查看账号数据库是否存在
        User user = userMapper.selectById(accountDTO);
        if(BeanUtil.isEmpty(user)){
            throw new BusinessException("NO_ACCOUNT","The current account is null");
        }
        // 3. 判断当前账户状态
        if(user.getRole()==2){
            throw new BusinessException("ACCOUNT_WRONG","The account is trouble");
        }
        // 4.生成JWT令牌
        String jwt = JwtUtil.generateToken(String.valueOf(user.getId()));
        RoleVo roleVo = new RoleVo();
        roleVo.setToken(jwt);
        roleVo.setRole(user.getRole());

        // 5.线程异步里边存数据
        String prefix = RedisConstant.PREFIX_USER + user.getId();
        CompletableFuture.runAsync(() -> makeToRedis(user, prefix));
        return roleVo;

    }

    private void makeToRedis(User user, String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("account", user.getAccount());
        map.put("role", String.valueOf(user.getRole()));
        redisTemplate.opsForHash().putAll(userId,map);
    }

}
