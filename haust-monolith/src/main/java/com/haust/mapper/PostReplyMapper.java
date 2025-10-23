package com.haust.mapper;

import com.haust.domain.po.PostReply;
import com.haust.domain.vo.ReplyVO;
import com.haust.mq.msg.LikeMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface PostReplyMapper {
    PostReply selectById(Long id);

    void updateReplyTimes(PostReply postReply);

    void addPost(PostReply po);


    List<ReplyVO> page(Long id);

    void deleteById(Long id);

    void addLike(ArrayList<LikeMsg> list);

    List<ReplyVO> pageByComment(Long id);
}
