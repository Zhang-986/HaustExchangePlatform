package com.haust.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "内推信息VO")
public class CodingSharingVo {

    @ApiModelProperty("内推信息主键")
    Long id;
    @ApiModelProperty("内推码")
    String codeId;
    @ApiModelProperty("公司名字")
    String companyName;

    @ApiModelProperty("点击次数")
    Integer clickNumber;

    @ApiModelProperty("创建时间")
    LocalDateTime createDate;

}
