package com.haust.mapper;

import com.haust.domain.po.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    /**
     * 发布帖子
     * @param post
     */
    @Insert("insert into post (title, description, user_id,anonymity) " +
            "values (#{title},#{description},#{userId},#{anonymity})")
    void insert(Post post);
}
