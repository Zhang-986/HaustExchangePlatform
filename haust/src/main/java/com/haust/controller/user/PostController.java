package com.haust.controller.user;

import com.haust.domain.dto.CreatePostDTO;
import com.haust.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user/posts")
@Api(tags = "帖子部分接口")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    @PostMapping
    @ApiOperation("发布帖子")
    public void createPost(@RequestBody CreatePostDTO createPostDTO){
        log.info("发布帖子：{}",createPostDTO);
        postService.createPost(createPostDTO);
    }

    /**
     * 修改帖子
     * @param id
     * @param createPostDTO
     */
    @PutMapping("/{id}")
    @ApiOperation("更新帖子")
    public void UpdatePost(@PathVariable Long id,@RequestBody CreatePostDTO createPostDTO){
        log.info("修改帖子：{},{}",createPostDTO,id);
        postService.updatePost(createPostDTO,id);
    }
}
