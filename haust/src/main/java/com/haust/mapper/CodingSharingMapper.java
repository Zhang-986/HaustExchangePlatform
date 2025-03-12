package com.haust.mapper;

import com.haust.domain.po.CodingSharing;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodingSharingMapper {
    /**
     * 修改内推数据
     * @param codingSharing
     */
    void update(CodingSharing codingSharing);

    /**
     * 插入内推数据
     * @param codingSharing
     */
    void insert(CodingSharing codingSharing);
}
