package com.haust.forum.controller.user;

import com.haust.common.domain.dto.CreateReplyDTO;
import com.haust.common.domain.dto.ReplyDTO;
import com.haust.common.domain.vo.HotReplyVo;
import com.haust.common.domain.vo.PageVO;
import com.haust.common.domain.vo.ReplyVO;
import com.haust.service.RepliesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("分页查询对应评论")
    @GetMapping("")
    public PageVO<ReplyVO>page(ReplyDTO replyDTO){
        return repliesService.pageQuery(replyDTO);
    }
    @ApiOperation("点赞/取消评论")
    @PostMapping("{id}/like")
    public Integer likeOrNot(@PathVariable Long id,Integer flag,Long postId){
        return repliesService.likeOrNot(id,flag,postId);
    }
    @ApiOperation("查看当前帖子热评")
    @GetMapping("/hot/{id}")
    public HotReplyVo getHotReply(@PathVariable Long id){
        return repliesService.getHotReply(id);
    }
}
