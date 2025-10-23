package com.haust.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页查询所得数据")
public class PageVO<T> {
    @ApiModelProperty("内推数据")
    List<T> data;
    @ApiModelProperty("总条数")
    Integer total;
    @ApiModelProperty("当前页码")
    Integer page;
    @ApiModelProperty("每页条数")
    Integer pageSize;

}
