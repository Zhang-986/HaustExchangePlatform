package com.haust.referral.referral.controller;

import com.haust.common.domain.dto.PageDTO;
import com.haust.common.domain.vo.CodingSharingVo;
import com.haust.common.domain.vo.PageVO;
import com.haust.referral.service.CodingSharingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员-内推审核接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ReferralAdminController {
    private final CodingSharingService codingSharingService;

    @ApiOperation("分页查询信息")
    @GetMapping("/page")
    public PageVO<CodingSharingVo> pageVO(PageDTO pageDTO){
        return codingSharingService.pageByAdmin(pageDTO);
    }

    @ApiOperation("审查对应信息")
    @PutMapping("/permit")
    public void permit(Long id, Integer status){
        codingSharingService.permit(id,status);
    }
}
