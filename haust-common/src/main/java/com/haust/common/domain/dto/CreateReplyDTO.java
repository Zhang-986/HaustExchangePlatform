package com.haust.common.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "发表评论请求参数")
public class CreateReplyDTO {

    @NotNull(message = "帖子ID不能为空")
    @ApiModelProperty(value = "帖子ID", required = true, example = "123")
    private Long postId;

    @NotBlank(message = "评论内容不能为空")
    @ApiModelProperty(value = "评论内容", required = true, example = "好文章！")
    private String content;

    @ApiModelProperty(value = "回复的目标用户ID", example = "456")
    private Long targetUserId;

    @ApiModelProperty(value = "回复的目标评论ID", example = "789")
    private Long targetReplyId;

    @NotNull(message = "是否匿名不能为空")
    @ApiModelProperty(value = "是否匿名", required = true, example = "true")
    private Boolean anonymity = true; // 默认匿名
}