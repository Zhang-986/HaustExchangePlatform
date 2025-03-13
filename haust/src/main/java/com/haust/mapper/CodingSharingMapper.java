package com.haust.mapper;

import com.haust.domain.dto.PageDTO;
import com.haust.domain.po.CodingSharing;
import com.haust.domain.vo.CodingSharingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    // 获取详细信息
    CodingSharing getDetailById(Long id);


    List<CodingSharingVO> page(PageDTO pageDTO);

    void permitInfo(@Param("id")Long id,@Param("status") Integer status);

    void delete(@Param("id") Long id);

    List<CodingSharingVO> pageMyInfo(@Param("pageDTO") PageDTO pageDTO, @Param("userId") Long userId);
}
