package com.haust.service;

import com.haust.domain.dto.CreateReplyDTO;
import com.haust.domain.dto.ReplyDTO;
import com.haust.domain.vo.PageVO;
import com.haust.domain.vo.ReplyVO;

public interface RepliesService {
    void addReply(CreateReplyDTO createReplyDTO);

    PageVO<ReplyVO> pageQuery(ReplyDTO replyDTO);

    void deleteComment(Long id);
}
