package com.haust.forum.controller;

import com.haust.forum.service.RepliesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "管理员-评论管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ForumAdminController {
    private final RepliesService repliesService;

    @ApiOperation("删除评论")
    @DeleteMapping("/replies/{id}")
    public void deleteComment(@PathVariable Long id){
        repliesService.deleteComment(id);
    }
}
