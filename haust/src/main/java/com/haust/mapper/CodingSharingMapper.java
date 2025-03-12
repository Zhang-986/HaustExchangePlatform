package com.haust.mapper;

import com.haust.domain.dto.PageDTO;
import com.haust.domain.po.CodingSharing;
import com.haust.domain.vo.CodingSharingVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodingSharingMapper {
    //插入内推信息数据
    void insert(CodingSharing codingSharing);
    // 获取详细信息
    CodingSharing getDetailById(Long id);


    List<CodingSharingVO> page(PageDTO pageDTO);
}
