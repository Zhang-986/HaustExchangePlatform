package com.haust.forum.controller.user;

import com.haust.common.domain.dto.CreatePostDTO;
import com.haust.common.domain.dto.PageDTO;
import com.haust.common.domain.po.Post;
import com.haust.common.domain.vo.PageVO;
import com.haust.common.domain.vo.PostVO;
import com.haust.forum.service.PostService;
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

    /**
     * 获取帖子详情
     * @param id
     * @return
     */
    @ApiOperation("获取帖子详情")
    @GetMapping("/{id}")
    public PostVO getDetails(@PathVariable Long id){
        log.info("获取帖子详情:{}",id);
        PostVO postVO=postService.getById(id);
        return postVO;
    }

    /**
     * 我的帖子
     * @param pageDTO
     * @return
     */
    @GetMapping("/myPost")
    @ApiOperation("我的帖子")
    public PageVO<Post> myPost(PageDTO pageDTO){
        log.info("我的帖子{}",pageDTO);
        PageVO<Post> pageVO =postService.myPost(pageDTO);
        return pageVO;
    }

}
