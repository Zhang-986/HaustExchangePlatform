package com.haust.user.controller;

import com.haust.common.domain.dto.AccountDTO;
import com.haust.common.domain.vo.RoleVo;
import com.haust.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public void register(@Validated @RequestBody AccountDTO accountDTO){
        log.info("用户端注册-> {}",accountDTO);
        userService.register(accountDTO);
    }

    @ApiOperation("用户登入")
    @PostMapping("/login")
    public RoleVo login(@Validated @RequestBody AccountDTO accountDTO){
        log.info("用户端登入-> {}",accountDTO);
        return userService.loginByUser(accountDTO);
    }
    
    @ApiOperation("用户提问")
    @GetMapping("/info")
    public String info(@Validated String text){
        return userService.info(text);
    }
}
