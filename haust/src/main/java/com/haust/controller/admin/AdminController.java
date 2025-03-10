package com.haust.controller.admin;

import com.haust.domain.dto.AccountDTO;
import com.haust.domain.po.CodingSharing;
import com.haust.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
管理员接口
 */
@Slf4j
@Api(tags = "管理员接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    @ApiOperation("登入接口")
    @GetMapping("/login")
    public String login(@RequestBody AccountDTO accountDTO){
        log.info("管理员-登入接收{}",accountDTO);
        userService.loginByAdmin();
        return "asd";
    }
}
