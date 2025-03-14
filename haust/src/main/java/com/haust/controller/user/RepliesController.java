package com.haust.controller.user;

import com.haust.domain.dto.CreateReplyDTO;
import com.haust.service.RepliesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/replies")
@Api(tags = "评论区相关接口")
@Slf4j
@RequiredArgsConstructor
public class RepliesController {
    private final RepliesService repliesService;
    @ApiOperation("发表评论")
    @PostMapping("")
    public void addReply(@Validated @RequestBody CreateReplyDTO createReplyDTO){
        repliesService.addReply(createReplyDTO);
    }
}
