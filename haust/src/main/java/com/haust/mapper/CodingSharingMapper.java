package com.haust.mapper;

import com.haust.domain.po.CodingSharing;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodingSharingMapper {
    //插入内推信息数据
    void insert(CodingSharing codingSharing);
}
