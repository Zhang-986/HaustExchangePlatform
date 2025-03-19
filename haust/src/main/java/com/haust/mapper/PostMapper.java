package com.haust.mapper;

import com.github.pagehelper.Page;
import com.haust.domain.po.Post;
import com.haust.domain.po.PostReply;
import com.haust.domain.vo.PostVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PostMapper {
    /**
     * 发布帖子
     * @param post
     */
    @Insert("insert into post (title, description, user_id,anonymity) " +
            "values (#{title},#{description},#{userId},#{anonymity})")
    void insert(Post post);

    Post selectById(Long postId);


    void updateIdAndReplyTimes(PostReply po);

    /**
     * 修改帖子
     * @param post
     */
    void update(Post post);

    /**
     * 分页查询
     * @return
     */

    Page<Post> getAll(Integer orderBy);

    /**
     * 删除帖子
     *
     * @param id
     * @param userId
     */
    @Delete("delete from post where id =#{id} and user_id=#{userId}")
    void delete(@Param("id") Long id,@Param("userId") Long userId);

    /**
     * 点赞取消点赞
     * @param id
     * @param times
     */
    @Update("update post set liked_times=liked_times+#{times} where id = #{id }")
    void updateLikesById(@Param("id") Integer id,@Param("times") Integer times);

    /**
     * 查询点赞数量
     * @param id
     * @return
     */
    @Select("select liked_times from post where id = #{id}")
    Integer getLikeTimesById(@Param("id") Integer id);

    /**
     * 获取帖子详情
     * @param id
     * @return
     */
    @Select("select * from post where id = #{id}")
    Post getById(Long id);
}
