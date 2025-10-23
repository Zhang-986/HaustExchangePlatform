package com.haust.service;

import com.haust.domain.dto.CreateReplyDTO;
import com.haust.domain.dto.ReplyDTO;
import com.haust.domain.vo.HotReplyVo;
import com.haust.domain.vo.PageVO;
import com.haust.domain.vo.ReplyVO;
import com.haust.mq.msg.LikeMsg;

public interface RepliesService {
    void addReply(CreateReplyDTO createReplyDTO);

    PageVO<ReplyVO> pageQuery(ReplyDTO replyDTO);

    void deleteComment(Long id);

    Integer likeOrNot(Long id, Integer flag,Long postId);

    HotReplyVo getHotReply(Long id);

    void addLike(LikeMsg likeMsg);
}
