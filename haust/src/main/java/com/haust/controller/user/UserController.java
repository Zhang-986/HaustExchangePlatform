package com.haust.controller.user;

import com.haust.domain.dto.CodingSharingDTO;
import com.haust.domain.vo.PageVO;
import com.haust.service.CodingSharingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
@Slf4j
@RequiredArgsConstructor
public class UserController {
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
