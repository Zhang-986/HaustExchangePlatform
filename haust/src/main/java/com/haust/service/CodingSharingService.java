package com.haust.service;

import com.haust.domain.dto.CodingSharingDTO;

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
}
