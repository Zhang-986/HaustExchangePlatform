package com.haust.mapper;

import com.haust.domain.po.PostReply;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostReplyMapper {
    PostReply selectById(Long id);

    void updateReplyTimes(PostReply postReply);

    void addPost(PostReply po);
}
