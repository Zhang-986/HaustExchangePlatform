package com.haust.service.impl;

import cn.hutool.core.codec.BCD;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.haust.annotation.SensitiveMonitor;
import com.haust.context.BaseContext;
import com.haust.domain.dto.CreatePostDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.enumeration.ContentType;
import com.haust.domain.po.Post;
import com.haust.domain.vo.PageVO;
import com.haust.domain.vo.PostVO;
import com.haust.mapper.PostMapper;
import com.haust.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @SensitiveMonitor(ContentType.post)
    @Override
    public void createPost(CreatePostDTO createPostDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(createPostDTO,post);
        post.setUserId(BaseContext.getId());
        StringBuilder stringBuilder = new StringBuilder();
        postMapper.insert(post);
    }

    /**
     * 修改帖子
     * @param id
     * @param createPostDTO
     */
    @SensitiveMonitor(ContentType.post)
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
        Page<Post> page= postMapper.getAll(pageDTO.getOrderBy(),null);

        //匿名帖子也返回userId
        List date=page.getResult();
/*        List collect = date.stream().map((x) -> {
            if (x.getAnonymity()) {
                x.setUserId(null);
            }
            return x;
        }).collect(Collectors.toList());*/

        PageVO result = PageVO.builder()
                .page(pageDTO.getPage())
                .pageSize(page.getPageSize())
                .total((int) page.getTotal())
                .data(date)
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

    /**
     * 点赞与取消
     * @param id
     * @param flag
     * @return
     */
    @Override
    @Transactional
    public Integer like(Integer id, Integer flag) {

        //点赞量增加数量
        if (flag!=1) flag=-1;
        postMapper.updateLikesById(id,flag);
        return postMapper.getLikeTimesById(id);
    }

    /**
     * 获取帖子详情
     * @param id
     * @return
     */
    @Override
    public PostVO getById(Long id) {
        //查询到帖子并根据匿名删除userId
        Post post = postMapper.getById(id);
        if (post.getAnonymity()) post.setUserId(null);

        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post,postVO);

        return postVO;
    }

    /**
     * 我的帖子
     * @param pageDTO
     * @return
     */
    @Override
    public PageVO<Post> myPost(PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPage(),pageDTO.getPageSize());
        //得到userId
        Long userId = BaseContext.getId();
        //根据排序方式查询
        Page<Post> page= postMapper.getAll(pageDTO.getOrderBy(),userId);

        //匿名帖子也返回userId
        List date=page.getResult();


        PageVO result = PageVO.builder()
                .page(pageDTO.getPage())
                .pageSize(page.getPageSize())
                .total((int) page.getTotal())
                .data(date)
                .build();
        return result;
    }
}
