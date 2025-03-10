package com.haust.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "User", description = "用户表")
public class User {
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "后期用来专门给user表进行水平拓展的", example = "1001")
    private Long addationalId;

    @ApiModelProperty(value = "用户账号", example = "user123")
    private String account;

    @ApiModelProperty(value = "密码", example = "password123")
    private String password;

    @ApiModelProperty(value = "性别,1->男，0->女", example = "1")
    private Integer sex;

    @ApiModelProperty(value = "角色,0->管理员，1->普通用户,2->问题账户", example = "1")
    private Integer role;
}