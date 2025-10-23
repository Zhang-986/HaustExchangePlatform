package com.haust.referral.referral.controller;

import com.haust.common.domain.dto.CodingSharingDTO;
import com.haust.common.domain.dto.PageDTO;
import com.haust.common.domain.po.CodingSharing;
import com.haust.common.domain.vo.CodingSharingVo;
import com.haust.common.domain.vo.PageVO;
import com.haust.referral.service.CodingSharingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "内推相关接口")
@Slf4j
@RequiredArgsConstructor
public class ReferralController {
    private final CodingSharingService codingSharingService;

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
    @GetMapping("/page")
    public PageVO<CodingSharingVo> pageQuery(PageDTO pageDTO){
        return codingSharingService.page(pageDTO);
    }
    
    @ApiOperation("删除信息")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        codingSharingService.delete(id);
    }
    
    @ApiOperation("查看用户内推信息")
    @GetMapping("/myInfo")
    public PageVO<CodingSharing> pageMyInfo(PageDTO pageDTO){
        return codingSharingService.pageMyInfo(pageDTO);
    }
}
