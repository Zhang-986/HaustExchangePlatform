package com.haust.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户提交内推信息")
public class CodingSharingDTO {

    @ApiModelProperty(value = "主键",example = "1")
    private Long id;
    @ApiModelProperty(value = "公司名字", example = "华为技术有限公司")
    private String companyName;
    @ApiModelProperty(value = "详细内推信息" , example = "里边主要负责后端开发")
    private String detail;
    @ApiModelProperty(value = "备注信息" , example = "这家公司感觉一般般")
    private String remark;
    @ApiModelProperty(value = "推荐者邮箱地址",example = "2332@129.com")
    private String recommanderEmail;

    @ApiModelProperty(value = "推荐指数",example = "1,2,3")
    private Integer recommandIndex;

    @ApiModelProperty(value = "内推码" , example = "2231")
    private String codeId;
}
