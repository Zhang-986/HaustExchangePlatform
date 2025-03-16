package com.haust.mapper;

import com.haust.domain.po.PostReply;
import com.haust.domain.vo.ReplyVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostReplyMapper {
    PostReply selectById(Long id);

    void updateReplyTimes(PostReply postReply);

    void addPost(PostReply po);


    List<ReplyVO> page(Long id);

    void deleteById(Long id);
}
