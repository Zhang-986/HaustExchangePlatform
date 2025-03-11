package com.haust.controller.user;

import com.haust.domain.dto.AccountDTO;
import com.haust.domain.vo.PageVO;
import com.haust.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Api(tags = "用户相关接口")
public class UserController {
    private final UserService userService;
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public void register(@Validated @RequestBody AccountDTO accountDTO){
        log.info("用户端-> {}",accountDTO);
        userService.register(accountDTO);
    }
    @ApiOperation("aa用户注册")
    @PostMapping("/aregister")
    public String register(){
        return "ok";
    }
}
