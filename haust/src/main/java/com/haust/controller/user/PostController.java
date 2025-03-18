package com.haust.controller.user;

import com.haust.annotation.SensitiveMonitor;
import com.haust.domain.dto.CreatePostDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.enumeration.ContentType;
import com.haust.domain.po.Post;
import com.haust.domain.vo.PageVO;
import com.haust.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.data.querydsl.QPageRequest;
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

    /**
     * 分页查询帖子
     * @param pageDTO
     * @return
     */
    @GetMapping
    @ApiOperation("分页查询帖子")
    public PageVO<Post> pageQuery(PageDTO pageDTO){
        log.info("分页查询帖子{}",pageDTO);
        PageVO<Post> pageVO =postService.page(pageDTO);
        return pageVO;
    }

    /**
     * 删除帖子
     * @param id
     */
    @ApiOperation("删除帖子")
    @DeleteMapping("/{id}")
    public void removePost(@PathVariable Long id){
        log.info("删除帖子：{}",id);
        postService.delete(id);
    }

    /**
     * 点赞取消点赞
     * @param flag
     * @return
     */
    @ApiOperation("点赞取消点赞帖子")
    @PostMapping("/{id}/like")
    public Integer like(@PathVariable Integer id  ,  Integer flag){
        log.info("点赞取消点赞帖子");
        Integer likedTimes= postService.like(id,flag);
        return likedTimes;
    }

}
