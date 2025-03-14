package com.haust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.haust.context.BaseContext;
import com.haust.domain.dto.CreateReplyDTO;
import com.haust.domain.po.PostReply;
import com.haust.exception.BusinessException;
import com.haust.mapper.PostReplyMapper;
import com.haust.service.RepliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class RepliesServiceImpl implements RepliesService {
    private final PostReplyMapper postReplyMapper;
    @Override
    public void addReply(CreateReplyDTO createReplyDTO) {
        // 1. 检查DTO是否为空
        if(BeanUtil.isEmpty(createReplyDTO)){
            throw new BusinessException("DTO IS NULL","TRY AGAIN");
        }
        // 2. 判断是帖子还是评论
        Long targetReplyId = createReplyDTO.getTargetReplyId();
        if(targetReplyId==null){
            // 3 是给帖子评论的
            post(createReplyDTO);
            return;
        }
        // 3 是给评论评论的
        reply(createReplyDTO);
        return;
    }

    private void reply(CreateReplyDTO createReplyDTO) {
        // 1. 获取当前用户ID
        Long userId = BaseContext.getId();
        // 2. 得到对应评论ID
        Long id = createReplyDTO.getTargetReplyId();
        // 3. 数据库查找对应评论ID
        PostReply postReply = postReplyMapper.selectById(id);
        if(BeanUtil.isEmpty(postReply)){
            throw new BusinessException("THE CURRENT OBJ IS NULL","TRY AGAGIN");
        }
        // 4. 复制给PO类
        PostReply po = BeanUtil.toBean(createReplyDTO, PostReply.class);
        po.setAnswerId(postReply.getId());
        po.setUserId(userId);
        // 5.插入表中
        postReplyMapper.addPost(po);
        // 998. 对已有字段进行更新
        postReply.setReplyTimes(postReply.getReplyTimes()+1);
        // 999. 对已有评论进行更新
        postReplyMapper.updateReplyTimes(postReply);
    }

    private void post(CreateReplyDTO createReplyDTO) {
        // 1. 获取当前当前用户ID
        Long userId = BaseContext.getId();
        // 2  获取目标帖子信息

        // 3. 更新最新回答ID

        // 4. 增加问题下的回答数量
    }
}
