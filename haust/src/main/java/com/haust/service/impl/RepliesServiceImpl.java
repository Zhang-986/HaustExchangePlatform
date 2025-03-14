package com.haust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.haust.context.BaseContext;
import com.haust.domain.dto.CreateReplyDTO;
import com.haust.domain.po.Post;
import com.haust.domain.po.PostReply;
import com.haust.exception.BusinessException;
import com.haust.mapper.PostMapper;
import com.haust.mapper.PostReplyMapper;
import com.haust.service.RepliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RepliesServiceImpl implements RepliesService {
    private final PostMapper postMapper;
    private final PostReplyMapper postReplyMapper;
    /*
        事务失效的原因
        1. 最低级的原因：没有被Spring管理
        2. 异常类型不对：要被RuntimeException
        3. 非事务方法调用事务注解方法: 没有经过一个Beans在AOP中被强化
        4. 非public方法修饰,
        5. 事务传播方法不对
     */
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
            ((RepliesServiceImpl) AopContext.currentProxy()).post(createReplyDTO);
            return;
        }
        // 3 是给评论评论的
        ((RepliesServiceImpl) AopContext.currentProxy()).reply(createReplyDTO);
    }

    @Transactional
    public void reply(CreateReplyDTO createReplyDTO) {
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
        // 6. 对已有字段进行更新
        postReply.setReplyTimes(postReply.getReplyTimes()+1);
        // 7. 对已有评论进行更新
        postReplyMapper.updateReplyTimes(postReply);
    }

    @Transactional
    public void post(CreateReplyDTO createReplyDTO) {
        // 1. 获取当前当前用户ID
        Long userId = BaseContext.getId();
        // 2. 转成PO类处理
        PostReply po = BeanUtil.toBean(createReplyDTO, PostReply.class);
        po.setAnswerId(createReplyDTO.getTargetReplyId());
        po.setUserId(userId);
        postReplyMapper.addPost(po);
        // 2  获取目标帖子信息
        Long postId = createReplyDTO.getPostId();
        Post post = postMapper.selectById(postId);
        if(BeanUtil.isEmpty(post)){
            throw new BusinessException("THE CURRENT OBJ IS NULL","TRY AGAGIN");
        }
        // 3. 更新最新回答ID,增加问题下的回答数量
        postMapper.updateIdAndReplyTimes(po);
    }
}
