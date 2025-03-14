package com.haust.domain.po;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 帖子表
* @TableName post
*/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post  {

    /**
    * 主键，帖子的id
    */
    @ApiModelProperty("主键，帖子的id")
    private Long id;
    /**
    * 帖子的标题
    */
    @ApiModelProperty("帖子的标题")
    private String title;

    @ApiModelProperty("帖子详细信息") private String description;
    /**
    * 用户id
    */
    @ApiModelProperty("用户id")
    private Long userId;
    /**
    * 最新的一个回答的id
    */
    @ApiModelProperty("最新的一个回答的id")
    private Long latestAnswerId;
    /**
    * 问题下的回答数量
    */
    @ApiModelProperty("问题下的回答数量")
    private Integer answerTimes;
    /**
    * 是否匿名，默认true
    */
    @ApiModelProperty("是否匿名，默认true")
    private Boolean anonymity;
    /**
    * 浏览次数
    */
    @ApiModelProperty("浏览次数")
    private Long clickCount;
    /**
    * 点赞数量
    */
    @ApiModelProperty("点赞数量")
    private Integer likedTimes;
    /**
    * 提问时间
    */
    @ApiModelProperty("提问时间")
    private LocalDateTime createTime;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;


}
