package com.haust.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiModel(description = "角色返回信息")
public class RoleVo {
    @ApiModelProperty("token值")
    public String token;
    @ApiModelProperty("role角色")
    public Integer role;
}
