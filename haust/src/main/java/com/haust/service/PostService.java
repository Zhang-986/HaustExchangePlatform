package com.haust.service;

import com.haust.domain.dto.CreatePostDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.po.Post;
import com.haust.domain.vo.PageVO;
import com.haust.domain.vo.PostVO;

public interface PostService {
    /**
     * 发布帖子
     * @param createPostDTO
     */
    void createPost(CreatePostDTO createPostDTO);


    /**
     * 修改帖子
     * @param createPostDTO
     * @param id
     */
    void updatePost(CreatePostDTO createPostDTO,Long id);

    /**
     * 分页查询
     * @param pageDTO
     * @return
     */
    PageVO<Post> page(PageDTO pageDTO);

    /**
     * 删除帖子
     * @param id
     */
    void delete(Long id);

    /**
     * 点赞与取消
     * @param id
     * @param flag
     * @return
     */
    Integer like(Integer id, Integer flag);

    /**
     * 获取帖子详情
     * @param id
     * @return
     */
    PostVO getById(Long id);
}
