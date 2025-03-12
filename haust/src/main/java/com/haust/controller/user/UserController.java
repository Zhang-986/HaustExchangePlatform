package com.haust.controller.user;

import com.haust.domain.dto.CodingSharingDTO;
import com.haust.domain.dto.AccountDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.vo.CodingSharingVO;
import com.haust.domain.vo.PageVO;
import com.haust.service.CodingSharingService;
import com.haust.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CodingSharingService codingSharingService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public void register(@Validated @RequestBody AccountDTO accountDTO){
        log.info("用户端注册-> {}",accountDTO);
        userService.register(accountDTO);
    }

    /**
     * 用户提交内推信息
     * @param codingSharingDTO
     */
    @ApiOperation("用户提交内推信息")
    @PostMapping("/addInfo")
    public void addInfo(@Validated @RequestBody CodingSharingDTO codingSharingDTO) {
        log.info("用户提交内推信息：{}", codingSharingDTO);
        codingSharingService.addInfo(codingSharingDTO);
    }
    @ApiOperation("用户登入")
    @PostMapping("/login")
    public String login(@Validated @RequestBody AccountDTO accountDTO){
        log.info("用户端登入-> {}",accountDTO);
        return userService.loginByUser(accountDTO);
    }

    /**
     * 修改内推信息
     * @param codingSharingDTO
     */
    @ApiOperation("修改内推信息")
    @PutMapping("/modify")
    public void modify(@Validated @RequestBody CodingSharingDTO codingSharingDTO) {
        log.info("修改内推信息:{}", codingSharingDTO);
        codingSharingService.modify(codingSharingDTO);
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public PageVO<CodingSharingVO> pageQuery(@RequestBody PageDTO pageDTO){
        return codingSharingService.page(pageDTO);
    }
}
