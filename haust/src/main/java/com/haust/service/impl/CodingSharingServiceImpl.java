package com.haust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.haust.context.BaseContext;
import com.haust.domain.dto.CodingSharingDTO;
import com.haust.domain.po.CodingSharing;
import com.haust.exception.BusinessException;
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
    @Override
    public CodingSharing getDetail(Long id) {
        // 1. 判断是否为空
        if(BeanUtil.isEmpty(id)){
            throw new BusinessException("Id IS NULL ","TRY AGAGIN");
        }
        // 2. 查询对应ID的数据信息
        CodingSharing codingSharing = codingSharingMapper.getDetailById(id);
        if(BeanUtil.isEmpty(codingSharing)){
            throw new BusinessException("THERE IS NULL","TRY AGAGIN");
        }
        return codingSharing;
    }
}
