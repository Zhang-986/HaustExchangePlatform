package com.haust.service;

import com.haust.domain.dto.CreatePostDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.po.Post;
import com.haust.domain.vo.PageVO;

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
}
