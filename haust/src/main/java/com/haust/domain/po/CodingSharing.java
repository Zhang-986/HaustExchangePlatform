package com.haust.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "conding_sharing",description = "交流信息表")
public class CodingSharing {
    @ApiModelProperty(value = "用户ID",example = "1")
    private Long userId;
    @ApiModelProperty(value = "公司名字", example = "华为技术有限公司")
    private String companyName;
    @ApiModelProperty(value = "详细内推信息" , example = "里边主要负责后端开发")
    private String detail;
    @ApiModelProperty(value = "备注信息" , example = "这家公司感觉一般般")
    private String remark;
    @ApiModelProperty(value = "推荐者邮箱地址",example = "2332@129.com")
    private String recommanderEmail;
    @ApiModelProperty(value = "浏览次数" ,example = "1242")
    private Integer clickNumber;
    @ApiModelProperty(value = "推荐指数",example = "1,2,3")
    private Integer recommanIndex;
    @ApiModelProperty(value = "状态" ,example = "0是未审核,1是已通过，-1是未通过")
    private Integer status;
    @ApiModelProperty(value = "内推码" , example = "2231")
    private String codeId;
}
