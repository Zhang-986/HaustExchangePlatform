package com.haust.controller.user;

import com.haust.domain.dto.CodingSharingDTO;
import com.haust.domain.dto.AccountDTO;
import com.haust.domain.vo.PageVO;
import com.haust.service.CodingSharingService;
import com.haust.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("用户端-> {}",accountDTO);
        userService.register(accountDTO);
    }

    private final CodingSharingService codingSharingService;
    /**
     * 用户提交内推信息
     * @param codingSharingDTO
     */
    @ApiOperation("用户提交内推信息")
    public void addInfo(@RequestBody CodingSharingDTO codingSharingDTO){
        log.info("用户提交内推信息：{}",codingSharingDTO);
        codingSharingService.addInfo(codingSharingDTO);

    }
}
