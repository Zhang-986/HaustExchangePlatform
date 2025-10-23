package com.haust.referral.service;

import com.haust.common.domain.dto.CodingSharingDTO;
import com.haust.common.domain.dto.PageDTO;
import com.haust.common.domain.po.CodingSharing;
import com.haust.common.domain.vo.CodingSharingVo;
import com.haust.common.domain.vo.PageVO;

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

    PageVO<CodingSharingVo> page(PageDTO pageDTO);

    PageVO<CodingSharingVo> pageByAdmin(PageDTO pageDTO);

    void permit(Long id, Integer status);

    void delete(Long id);

    PageVO<CodingSharing> pageMyInfo(PageDTO pageDTO);
}
