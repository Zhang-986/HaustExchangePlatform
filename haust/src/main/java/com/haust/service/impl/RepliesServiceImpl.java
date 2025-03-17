package com.haust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haust.constant.RedisConstant;
import com.haust.context.BaseContext;
import com.haust.domain.dto.CreateReplyDTO;
import com.haust.domain.dto.ReplyDTO;
import com.haust.domain.po.Post;
import com.haust.domain.po.PostReply;
import com.haust.domain.vo.HotReplyVo;
import com.haust.domain.vo.PageVO;
import com.haust.domain.vo.ReplyVO;
import com.haust.exception.BusinessException;
import com.haust.mapper.PostMapper;
import com.haust.mapper.PostReplyMapper;
import com.haust.service.RepliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RepliesServiceImpl implements RepliesService {
    private final StringRedisTemplate stringRedisTemplate;
    private final PostMapper postMapper;
    private final PostReplyMapper postReplyMapper;


    /**
     * 事务失效的原因
     * 1. 最低级的原因：没有被Spring管理
     * 2. 异常类型不对：要被RuntimeException
     * 3. 非事务方法调用事务注解方法: 没有经过一个Beans在AOP中被强化
     * 4. 非public方法修饰,
     * 5. 事务传播方法不对
     */
    @Override
    public void addReply(CreateReplyDTO createReplyDTO) {
        // 1. 检查DTO是否为空
        if (BeanUtil.isEmpty(createReplyDTO)) {
            throw new BusinessException("DTO IS NULL", "TRY AGAIN");
        }
        // 2. 判断是帖子还是评论
        Long targetReplyId = createReplyDTO.getTargetReplyId();
        if (targetReplyId == null) {
            // 3 是给帖子评论的
            // TODO 事务失效
            post(createReplyDTO);
            return;
        }
        // 3 是给评论评论的
        // TODO 事务失效
        reply(createReplyDTO);
    }

    /**
     * zzk
     *
     * @param replyDTO
     * @return
     */
    @Override
    public PageVO<ReplyVO> pageQuery(ReplyDTO replyDTO) {
        // 1. 进行当前对象的判断
        if (BeanUtil.isEmpty(replyDTO)) {
            throw new BusinessException("THE ITEM IS NULL ,", "TRY AGAIN");
        }
        // 2.进行分页参数处理
        PageHelper.startPage(replyDTO.getPage(), replyDTO.getPageSize());
        // 3. 查询数据
        if (!replyDTO.getFlag().equals("post") && !replyDTO.equals("comment")) {
            throw new BusinessException("WRONG VOCABULARY", "TRY AGAIN");
        }
        List<ReplyVO> list = postReplyMapper.page(replyDTO.getId());
        // 业务层进行处理两种不同的数据
        // 4. 处理查询帖子评论
        return replyDTO.getFlag().equals("post") ? postInfo(list) : commentInfo(list);
    }

    @Override
    public void deleteComment(Long id) {
        // 1. 判断当前是否为管理员
        if (BaseContext.getId().equals("1")) {
            throw new BusinessException("WRONG ROLE", "越权操作");
        }
        // 2. 检验参数
        if (id == null) {
            throw new BusinessException("WRONG ITEM ", "数据库无对应数据");
        }
        // 3. 进行处理
        postReplyMapper.deleteById(id);
    }

    @Override
    public Integer likeOrNot(Long id, Integer flag, Long post_id) {
        // 0. 获取当前线程用户ID
        Long userId = BaseContext.getId();

        // 1. 检验参数是否有效
        if (id == null || flag == null) {
            throw new BusinessException("THE PARAMETER IS NULL", "TRY AGAIN");
        }

        // 2. 构造 Key
        String key = RedisConstant.REPLY_LIKE + id; // SET 的 Key

        // 3. 构造 Value
        String userValue = RedisConstant.PREFIX_USER + userId; // SET 中的 Value

        // 4. 根据 flag 执行点赞或取消点赞操作
        return flag == 1 ? like(key, userValue, post_id) : notlike(key, userValue, post_id);
    }

    @Override
    public HotReplyVo getHotReply(Long id) {
        // 1.检验id是否有效
        if (id == null) {
            throw new BusinessException("EXCEPTION", "User is not found");
        }
        // 2.从当前帖子的redis数据库中找到点赞里最多的评论
        Set<ZSetOperations.TypedTuple<String>> tuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(RedisConstant.REPLY_CONTENT + id, 0, 0);
        if (BeanUtil.isEmpty(tuples)) {
            return null;
        }
        // 3.拿到信息
        ZSetOperations.TypedTuple<String> next = tuples.iterator().next();
        String key = next.getValue();
        String[] split = key.split(":");
        Long replyId = Long.valueOf(split[2]);
        System.out.println(replyId);
        // 4.检索数据库拿到最新值
        PostReply postReply = postReplyMapper.selectById(replyId);
        if (BeanUtil.isEmpty(postReply)) {
            throw new BusinessException("TRY AGAIN", "THE ITEM IS NULL");
        }
        // 5.包装值
        HotReplyVo hotReplyVo = BeanUtil.toBean(postReply, HotReplyVo.class);
        return hotReplyVo;
    }

    private Integer like(String key, String userValue, Long post_id) {
        // 1. 检查用户是否已点赞
        if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, userValue))) {
            throw new BusinessException("EXCEPTION", "User has already liked this reply");
        }

        // 2. 添加用户到 SET 中
        stringRedisTemplate.opsForSet().add(key, userValue);

        // 3. 返回点赞总数
        return getLikeCount(key, post_id);
    }

    private Integer notlike(String key, String userValue, Long post_id) {
        // 1. 检查用户是否已点赞
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForSet().isMember(key, userValue))) {
            throw new BusinessException("EXCEPTION", "User has not liked this reply");
        }
        // 2. 从 SET 中移除用户
        stringRedisTemplate.opsForSet().remove(key, userValue);

        // 3. 返回点赞总数
        return getLikeCount(key, post_id);
    }

    private Integer getLikeCount(String key, Long post_id) {
        // 4. 获取 SET 的大小，即点赞数量
        Long count = stringRedisTemplate.opsForSet().size(key);
        // 5. 热评榜单
        stringRedisTemplate.opsForZSet().add(RedisConstant.REPLY_CONTENT + post_id, key, count);

        return count != null ? count.intValue() : 0;
    }

    /*
    处理评论的评论
     */
    private PageVO<ReplyVO> commentInfo(List<ReplyVO> list) {
        //   如果answer_id == null and target_reply_id == null 说明查的是这个贴子的ID
        List<ReplyVO> list1 = list.stream().filter(replyVO -> replyVO.getTargetReplyId() != null).collect(Collectors.toList());
        PageInfo<ReplyVO> replyVOPageInfo = new PageInfo<>(list1);
        return getAll(replyVOPageInfo);
    }

    private PageVO<ReplyVO> getAll(PageInfo<ReplyVO> replyVOPageInfo) {
        //  进行分页的处理包装
        PageVO<ReplyVO> pageVO = new PageVO<>();
        pageVO.setData(replyVOPageInfo.getList());
        pageVO.setPage(replyVOPageInfo.getPages());
        pageVO.setPageSize(replyVOPageInfo.getPageSize());
        pageVO.setTotal((int) replyVOPageInfo.getTotal()); // 设置总条数
        return pageVO;
    }

    /*
    处理帖子的评论
     */
    private PageVO<ReplyVO> postInfo(List<ReplyVO> list) {
        List<ReplyVO> list1 = list.stream().filter(replyVO -> replyVO.getTargetReplyId() == null).collect(Collectors.toList());
        PageInfo<ReplyVO> replyVOPageInfo = new PageInfo<>(list1);
        return getAll(replyVOPageInfo);
    }

    @Transactional
    public void reply(CreateReplyDTO createReplyDTO) {
        // 1. 获取当前用户ID
        Long userId = BaseContext.getId();
        // 2. 得到对应评论ID
        Long id = createReplyDTO.getTargetReplyId();
        // 3. 数据库查找对应评论ID
        PostReply postReply = postReplyMapper.selectById(id);
        if (BeanUtil.isEmpty(postReply)) {
            throw new BusinessException("THE CURRENT OBJ IS NULL", "TRY AGAGIN");
        }
        // 4. 复制给PO类
        PostReply po = BeanUtil.toBean(createReplyDTO, PostReply.class);
        po.setAnswerId(postReply.getId());
        po.setUserId(userId);
        // 5.插入表中
        postReplyMapper.addPost(po);
        // 6. 对已有字段进行更新
        postReply.setReplyTimes(postReply.getReplyTimes() + 1);
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
        if (BeanUtil.isEmpty(post)) {
            throw new BusinessException("THE CURRENT OBJ IS NULL", "TRY AGAGIN");
        }
        // 3. 更新最新回答ID,增加问题下的回答数量
        postMapper.updateIdAndReplyTimes(po);
    }
}
