package com.haust.common.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
@ApiModel(description = "传递查询参数")
public class PageDTO {
    @ApiModelProperty("页码")
    Integer page;
    @ApiModelProperty("一页展示数据")
    Integer pageSize;
    @ApiModelProperty("排序方式")
    Integer orderBy;
    @ApiModelProperty("状态")
    Integer status;
}
