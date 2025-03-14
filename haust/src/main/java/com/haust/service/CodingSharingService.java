package com.haust.service;

import com.haust.domain.dto.CodingSharingDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.po.CodingSharing;
import com.haust.domain.vo.CodingSharingVo;
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

    com.haust.domain.po.CodingSharing getDetail(Long id);

    PageVO<CodingSharingVo> page(PageDTO pageDTO);

    PageVO<CodingSharingVo> pageByAdmin(PageDTO pageDTO);

    void permit(Long id, Integer status);

    void delete(Long id);

    PageVO<CodingSharing> pageMyInfo(PageDTO pageDTO);
}
