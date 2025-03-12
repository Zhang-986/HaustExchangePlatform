package com.haust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haust.context.BaseContext;
import com.haust.domain.dto.CodingSharingDTO;
import com.haust.domain.dto.PageDTO;
import com.haust.domain.po.CodingSharing;
import com.haust.domain.vo.CodingSharingVO;
import com.haust.domain.vo.PageVO;
import com.haust.exception.BusinessException;
import com.haust.mapper.CodingSharingMapper;
import com.haust.service.CodingSharingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public PageVO<CodingSharingVO> page(PageDTO pageDTO) {
        // 0.设置分页参数
        PageHelper.startPage(pageDTO.getPage(),pageDTO.getPageSize());
        // 1.判断参数是否为空
        if(BeanUtil.isEmpty(pageDTO)){
            throw new BusinessException("ITEM IS NULL ","TRY AGAGIN");
        }
        // 2.开始分页查询
        List<CodingSharingVO> list = codingSharingMapper.page(pageDTO);
        // 3.封装使用 PageInfo 获取分页信息
        PageInfo<CodingSharingVO> pageInfo = new PageInfo<>(list);
        // 4. 封装数据
        PageVO<CodingSharingVO> pageVO = new PageVO<>();
        pageVO.setData(pageInfo.getList());     // 设置当前内容
        pageVO.setPage(pageInfo.getPageNum()); // 设置当前页码
        pageVO.setPageSize(pageInfo.getPageSize()); // 设置每页条数
        pageVO.setTotal((int) pageInfo.getTotal()); // 设置总条数
        return pageVO;
    }

    /**
     * 修改内推信息
     * @param codingSharingDTO
     */
    @Override
    public void modify(CodingSharingDTO codingSharingDTO) {
        CodingSharing codingSharing = new CodingSharing();
        Long userId = BaseContext.getId();
        codingSharing.setUserId(userId);
        BeanUtils.copyProperties(codingSharingDTO,codingSharing);

        codingSharingMapper.update(codingSharing);
    }
}
