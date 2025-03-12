package com.haust.controller.all;

import com.haust.domain.po.CodingSharing;
import com.haust.service.CodingSharingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/*
管理员接口
 */
@Slf4j
@Api(tags = "通用接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/all")
public class AllController {
    private final CodingSharingService codingSharingService;

    @ApiOperation("查询详细接口")
    @GetMapping("/getDetail/{id}")
    public CodingSharing getDetail(@PathVariable Long id){
        log.info("通用接口-接收{}",id);
        CodingSharing codingSharing = codingSharingService.getDetail(id);
        return codingSharing;
    }
}
