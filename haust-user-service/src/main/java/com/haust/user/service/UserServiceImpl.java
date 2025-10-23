package com.haust.user.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.haust.user.annotation.LoginMonitor;
import com.haust.common.constant.RedisConstant;
import com.haust.common.context.BaseContext;
import com.haust.common.domain.dto.AccountDTO;
import com.haust.common.domain.dto.PageDTO;
import com.haust.common.domain.po.User;
import com.haust.common.domain.vo.PageVO;
import com.haust.common.domain.vo.RoleVo;
import com.haust.common.exception.BusinessException;
import com.haust.user.mapper.UserMapper;

import com.haust.user.mq.msg.UserMsg;
import com.haust.user.service.UserService;
;import com.haust.common.util.DifyApiUtil;
import com.haust.common.util.JwtUtil;
import com.haust.common.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final StringRedisTemplate redisTemplate;
    private final UserMapper userMapper;
    private final DifyApiUtil difyApiUtil;

    @Override
    public RoleVo loginByAdmin(AccountDTO accountDTO) {
        // 0.判断当前DTO是否为null
        if (BeanUtil.isEmpty(accountDTO)) {
            throw new BusinessException("LOGIN_EMPTY", "AccountDTO cannot be empty");
        }
        // 1.查看账号数据库是否存在
        User user = userMapper.selectById(accountDTO);
        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException("NO_ACCOUNT", "The current account is null");
        }
        // 2. 判断传入密码与加密后的密码
        if (!PasswordUtil.matches(accountDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("PASSWORD_WRONG", "The password is wrong");
        }
        // 2.5 判断当前账户状态
        if (user.getRole() == 2) {
            throw new BusinessException("ACCOUNT_WRONG", "The account is trouble");
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

    @LoginMonitor
    @Override
    public RoleVo loginByUser(AccountDTO accountDTO) {
        // 1. 判断当前DTO是否有效
        if (BeanUtil.isEmpty(accountDTO)) {
            throw new BusinessException("LOGIN_EMPTY", "AccountDTO cannot be empty");
        }
        // 2.查看账号数据库是否存在
        User user = userMapper.selectById(accountDTO);
        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException("NO_ACCOUNT", "The current account is null");
        }
        // 3. 判断当前账户状态
        if (user.getRole() == 2 || user.getRole() == 0) {
            throw new BusinessException("ACCOUNT_WRONG", "The account is trouble");
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

    @Transactional
    @Override
    public void addMonitor(UserMsg userMsg) {
        // 1.多一次数据库查询
        UserMsg user = userMapper.selectByName(userMsg);
        // 2.判断是否更新
        if (!BeanUtil.isEmpty(user)) {
            // 3.++ 处理
            userMapper.updateTimes(user);
            return;
        }
        // 4 没有记录就插入
        userMapper.solveTimes(userMsg);
    }

    @Override
    public PageVO<UserMsg> getMonitorLog(PageDTO pageDTO) {
        // 1.检验当前ID是否合法
        Long userId = BaseContext.getId();
        if (!userId.equals("0")) {
            throw new BusinessException("WRONG", "WRONG ROLE");
        }
        // 2.分页查询当前用户信息
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getPageSize());
        List<UserMsg> list = userMapper.pageByMonitor(pageDTO.getOrderBy());
        // 3.封装成PageInfo获取分页信息
        PageInfo<UserMsg> userMsgPageInfo = new PageInfo<>(list);
        // 4.开始包装
        PageVO<UserMsg> vo = new PageVO<>();
        vo.setData(userMsgPageInfo.getList());
        vo.setPage(userMsgPageInfo.getPageNum());
        vo.setPageSize(userMsgPageInfo.getPageSize());
        vo.setTotal((int) userMsgPageInfo.getTotal());
        return vo;
    }

    @Override
    public String info(String text) {
        log.info("Processing info request with text: {}", text);

        // 1.判断当前文本是否为null
        if (BeanUtil.isEmpty(text)) {
            throw new BusinessException("TEXT_EMPTY", "The text is empty");
        }

        // 2.调用API接口
        String result = null;
        try {
            log.info("Calling Dify API with userId: {}", BaseContext.getId());

            // 收集所有回复文本并组合起来
            result = difyApiUtil.streamChat(text, String.valueOf(BaseContext.getId()), null)
                    .doOnNext(chunk -> log.info("Received chunk: [{}]", chunk)) // 加方括号便于查看空格
                    .doOnComplete(() -> log.info("Stream completed"))
                    .collect(StringBuilder::new, StringBuilder::append) // 收集所有内容
                    .map(sb -> {
                        String content = sb.toString();
                        log.info("Final collected content length: {}", content.length());
                        return content;
                    })
                    .block();

            log.info("API call completed, result present: {}", (result != null));
        } catch (DifyApiUtil.DifyApiException e) {
            log.error("Dify API error", e);
            throw new RuntimeException("大模型服务调用失败: " + e.getMessage(), e);
        }

        if (result == null || result.isEmpty()) {
            log.warn("Dify API returned empty result for input: {}", text);
            return "很抱歉，处理您的请求时出现了问题，请稍后再试。";
        }

        return result;
    }


    private void makeToRedis(User user, String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("account", user.getAccount());
        map.put("role", String.valueOf(user.getRole()));
        map.put("time", String.valueOf(LocalDateTime.now()));
        redisTemplate.opsForHash().putAll(userId, map);
    }

}
