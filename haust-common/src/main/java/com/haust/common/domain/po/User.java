package com.haust.common.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
@ApiModel(value = "User", description = "用户表")
public class User {
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "用户账号", example = "user123")
    private String account;

    @ApiModelProperty(value = "密码", example = "password123")
    private String password;

    @ApiModelProperty(value = "角色,0->管理员，1->普通用户,2->问题账户", example = "1")
    private Integer role;
}