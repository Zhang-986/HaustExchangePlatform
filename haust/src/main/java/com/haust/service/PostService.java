package com.haust.service;

import com.haust.domain.dto.CreatePostDTO;

public interface PostService {
    /**
     * 发布帖子
     * @param createPostDTO
     */
    void createPost(CreatePostDTO createPostDTO);
}
