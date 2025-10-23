package com.haust.common.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "用户创建帖子请求参数")
public class CreatePostDTO {

    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题长度不能超过255个字符")
    @ApiModelProperty(value = "帖子标题", required = true, example = "如何学习Spring Boot?")
    private String title;

    @Size(max = 2048, message = "描述长度不能超过2048个字符")
    @ApiModelProperty(value = "帖子详细信息", example = "求大佬指导学习路线...")
    private String description = ""; // 默认空字符串

    @ApiModelProperty(value = "是否匿名发布", example = "true")
    private Boolean anonymity = true; // 默认匿名
}