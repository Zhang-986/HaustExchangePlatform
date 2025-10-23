package com.haust.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页信息DTO")
public class ReplyDTO extends PageDTO{
    @ApiModelProperty("帖子ID或者评论ID")
     Long Id;
    @ApiModelProperty("当前处理的时候，是贴子的评论还是评论的评论")
     String flag;
}
