package com.haust.service.impl;

import com.haust.context.BaseContext;
import com.haust.domain.dto.CodingSharingDTO;
import com.haust.domain.po.CodingSharing;
import com.haust.mapper.CodingSharingMapper;
import com.haust.service.CodingSharingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodingSharingServiceImpl implements CodingSharingService {
    private final CodingSharingMapper codingSharingMapper;
    /**
     * 用户提交内推信息
     * @param codingSharingDTO
     */
    @Override
    public void addInfo(CodingSharingDTO codingSharingDTO) {
        CodingSharing codingSharing = new CodingSharing();
        BeanUtils.copyProperties(codingSharingDTO,codingSharing);

        codingSharing.setUserId(BaseContext.getId());
        codingSharingMapper.insert(codingSharing);
    }
}
