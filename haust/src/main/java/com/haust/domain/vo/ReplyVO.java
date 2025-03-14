package com.haust.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "评论信息")
public class ReplyVO {

    @ApiModelProperty(value = "评论ID", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "帖子ID", required = true, example = "123")
    private Long postId;

    @ApiModelProperty(value = "评论者ID", required = true, example = "1")
    private Long userId;

    @ApiModelProperty(value = "评论内容", required = true, example = "好文章！")
    private String content;

    @ApiModelProperty(value = "回复的目标用户ID", required = true, example = "456")
    private Long targetUserId;

    @ApiModelProperty(value = "回复的目标评论ID", example = "789")
    private Long targetReplyId;

    @ApiModelProperty(value = "是否匿名", required = true, example = "true")
    private Boolean anonymity;

    @ApiModelProperty(value = "回复数量", required = true, example = "5")
    private Integer replyTimes;

    @ApiModelProperty(value = "点赞数量", required = true, example = "10")
    private Integer likedTimes;

    @ApiModelProperty(value = "创建时间", required = true, example = "2023-09-01 10:00:00")
    private String createTime;
}