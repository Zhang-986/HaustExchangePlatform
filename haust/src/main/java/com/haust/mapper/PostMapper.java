package com.haust.mapper;

import com.github.pagehelper.Page;
import com.haust.domain.po.Post;
import com.haust.domain.po.PostReply;
import javafx.geometry.Pos;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
}
