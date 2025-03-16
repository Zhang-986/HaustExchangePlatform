package com.haust.controller.admin;

import com.haust.domain.dto.AccountDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.vo.CodingSharingVo;
import com.haust.domain.vo.PageVO;
import com.haust.domain.vo.RoleVo;
import com.haust.service.CodingSharingService;
import com.haust.service.RepliesService;
import com.haust.service.UserService;
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
@Api(tags = "管理员接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RepliesService repliesService;
    private final CodingSharingService codingSharingService;
    @ApiOperation("登入接口")
    @PostMapping("/login")
    public RoleVo login(@Validated @RequestBody AccountDTO accountDTO){
        log.info("管理员-登入接收{}",accountDTO);
        return userService.loginByAdmin(accountDTO);

    }
    @ApiOperation("分页查询信息")
    @GetMapping("/page")
    public PageVO<CodingSharingVo> pageVO(PageDTO pageDTO){
        log.info("管理员-分页接收{}",pageDTO);
        return codingSharingService.pageByAdmin(pageDTO);
    }
    @ApiOperation("审查对应信息")
    @PutMapping("/permit")
    public void permit(Long id, Integer status){
        codingSharingService.permit(id,status);
    }
    @ApiOperation("删除评论")
    @DeleteMapping("/replies/{id}")
    public void deleteComment(@PathVariable Long id){
        repliesService.deleteComment(id);
    }
}
