package com.haust.controller.admin;

import com.haust.domain.po.CodingSharing;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
管理员接口
 */

@Api(tags = "管理员接口")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @ApiOperation("登入接口")
    @GetMapping("/login")
    public String login(CodingSharing codingSharing){
        return "asd";
    }
}
