package com.haust.referral.mapper;

import com.haust.common.domain.dto.PageDTO;
import com.haust.common.domain.po.CodingSharing;
import com.haust.common.domain.vo.CodingSharingVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CodingSharingMapper {
    /**
     * 修改内推数据
     * @param codingSharing
     */
    void update(com.haust.domain.po.CodingSharing codingSharing);

    /**
     * 插入内推数据
     * @param codingSharing
     */
    void insert(com.haust.domain.po.CodingSharing codingSharing);
    // 获取详细信息
    com.haust.domain.po.CodingSharing getDetailById(Long id);


    List<CodingSharingVo> page(PageDTO pageDTO);

    void permitInfo(@Param("id")Long id,@Param("status") Integer status);

    void delete(@Param("id") Long id);

    List<CodingSharing> pageMyInfo(@Param("pageDTO") PageDTO pageDTO, @Param("userId") Long userId);
}
