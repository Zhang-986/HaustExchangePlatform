package com.haust.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "帖子信息返回对象")
public class PostVO implements Serializable {

    @ApiModelProperty(value = "帖子ID", example = "123")
    private Long id;

    @ApiModelProperty(value = "帖子标题", example = "如何学习Spring Boot?")
    private String title;

    @ApiModelProperty(value = "帖子详细信息", example = "求大佬指导学习路线...")
    private String description;

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;
    @ApiModelProperty(value = "最新的一个回答的id",example = "1")
    private Integer latestAnswerId;
    @ApiModelProperty(value = "问题下的回答数量", example = "124")
    private Integer answerTimes;

    @ApiModelProperty(value = "是否匿名", example = "true")
    private Boolean anonymity;

    @ApiModelProperty(value = "浏览次数", example = "0")
    private Long clickCount;

    @ApiModelProperty(value = "点赞数量", example = "0")
    private Integer likedTimes;

    @ApiModelProperty(value = "创建时间", example = "2023-09-01 10:00:00")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", example = "2023-09-01 10:00:00")
    private LocalDateTime updateTime;

}