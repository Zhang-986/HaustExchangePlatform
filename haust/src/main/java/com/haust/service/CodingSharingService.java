package com.haust.service;

import com.haust.domain.dto.CodingSharingDTO;
import com.haust.domain.po.CodingSharing;

public interface CodingSharingService {
    /**
     * 用户提交内推信息
     * @param codingSharingDTO
     */
    void addInfo(CodingSharingDTO codingSharingDTO);

    CodingSharing getDetail(Long id);
}
