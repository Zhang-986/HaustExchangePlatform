package com.haust.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "热评")
public class HotReplyVo implements Serializable {

    @ApiModelProperty(value = "帖子ID", example = "123")
    private Long id;

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "评论内容", required = true, example = "好文章！")
    private String content;

    @ApiModelProperty(value = "是否匿名", example = "true")
    private Boolean anonymity;

    @ApiModelProperty(value = "创建时间", example = "2023-09-01 10:00:00")
    private LocalDateTime createTime;
}