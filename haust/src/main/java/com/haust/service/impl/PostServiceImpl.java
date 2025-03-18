package com.haust.service.impl;

import cn.hutool.core.codec.BCD;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.haust.context.BaseContext;
import com.haust.domain.dto.CreatePostDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.po.Post;
import com.haust.domain.vo.PageVO;
import com.haust.mapper.PostMapper;
import com.haust.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 修改帖子
     * @param id
     * @param createPostDTO
     */
    @Override
    public void updatePost(CreatePostDTO createPostDTO,Long id) {
        Post post = new Post();
        BeanUtils.copyProperties(createPostDTO,post);
        post.setId(id);
        post.setUserId(BaseContext.getId());
        postMapper.update(post);
    }

    /**
     * 分页查询
     * @param pageDTO
     * @return
     */
    @Override
    public PageVO<Post> page(PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPage(),pageDTO.getPageSize());
        //根据排序方式查询
        Page<Post> page= postMapper.getAll(pageDTO.getOrderBy());
/*        System.out.println(page);
        return  null;*/
        //匿名帖子不返回userId
        List<Post> date=page.getResult();
        List collect = date.stream().map((x) -> {
            if (x.getAnonymity()) {
                x.setUserId(null);
            }
            return x;
        }).collect(Collectors.toList());

        PageVO result = PageVO.builder()
                .page(pageDTO.getPage())
                .pageSize(page.getPageSize())
                .total((int) page.getTotal())
                .data(collect)
                .build();
        return result;
    }

    /**
     * 删除帖子
     * @param id
     */
    @Override
    public void delete(Long id) {
        Long userId = BaseContext.getId();
        postMapper.delete(id,userId);
    }
}
