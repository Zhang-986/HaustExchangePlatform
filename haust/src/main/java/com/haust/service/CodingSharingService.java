package com.haust.service;

import com.haust.domain.dto.CodingSharingDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.po.CodingSharing;
import com.haust.domain.vo.CodingSharingVO;
import com.haust.domain.vo.PageVO;

public interface CodingSharingService {
    /**
     * 用户提交内推信息
     * @param codingSharingDTO
     */
    void addInfo(CodingSharingDTO codingSharingDTO);

    /**
     * 修改内推信息
     * @param codingSharingDTO
     */
    void modify(CodingSharingDTO codingSharingDTO);

    CodingSharing getDetail(Long id);

    PageVO<CodingSharingVO> page(PageDTO pageDTO);
}
