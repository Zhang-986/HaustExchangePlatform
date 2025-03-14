package com.haust.service.impl;

import com.haust.context.BaseContext;
import com.haust.domain.dto.CreatePostDTO;
import com.haust.domain.po.Post;
import com.haust.mapper.PostMapper;
import com.haust.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    /**
     * 发布帖子
     * @param createPostDTO
     */
    @Override
    public void createPost(CreatePostDTO createPostDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(createPostDTO,post);
        post.setUserId(BaseContext.getId());
        postMapper.insert(post);
    }
}
