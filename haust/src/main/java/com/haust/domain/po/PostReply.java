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
import sun.util.resources.LocaleData;

/**
* 帖子评论表
* @TableName post_reply
*/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReply  {

    /**
    * 帖子的回答id
    */
    @ApiModelProperty("帖子的回答id")
    private Long id;
    /**
    * 帖子id
    */
    @ApiModelProperty("帖子id")
    private Long post_id;
    /**
    * 回复的上级回答id
    */
    @ApiModelProperty("回复的上级回答id")
    private Long answer_id;
    /**
    * 回答者id
    */
    @ApiModelProperty("回答者id")
    private Long user_id;
    /**
    * 回答内容
    */
    @ApiModelProperty("回答内容")
    private String content;
    /**
    * 回复的目标用户id
    */
    @ApiModelProperty("回复的目标用户id")
    private Long target_user_id;
    /**
    * 回复的目标回复id
    */
    @ApiModelProperty("回复的目标回复id")
    private Long target_reply_id;
    /**
    * 评论数量
    */
    @ApiModelProperty("评论数量")
    private Integer reply_times;
    /**
    * 点赞数量
    */
    @ApiModelProperty("点赞数量")
    private Integer liked_times;
    /**
    * 是否匿名，默认true
    */
    @ApiModelProperty("是否匿名，默认true")
    private Boolean anonymity;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private LocalDateTime create_time;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    private LocalDateTime update_time;


}
