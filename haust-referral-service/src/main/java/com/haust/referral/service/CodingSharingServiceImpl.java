package com.haust.referral.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haust.referral.annotation.SensitiveMonitor;
import com.haust.common.context.BaseContext;
import com.haust.common.domain.dto.CodingSharingDTO;
import com.haust.common.domain.dto.PageDTO;
import com.haust.common.domain.enumeration.ContentType;
import com.haust.common.domain.po.CodingSharing;
import com.haust.common.domain.vo.CodingSharingVo;
import com.haust.common.domain.vo.PageVO;
import com.haust.common.exception.BusinessException;
import com.haust.referral.mapper.CodingSharingMapper;
import com.haust.referral.service.CodingSharingService;
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

    @SensitiveMonitor(ContentType.sharing)
    @Override
    public void addInfo(CodingSharingDTO codingSharingDTO) {
        com.haust.common.domain.po.CodingSharing codingSharing = new com.haust.common.domain.po.CodingSharing();
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
        com.haust.common.domain.po.CodingSharing codingSharing = codingSharingMapper.getDetailById(id);
        if(BeanUtil.isEmpty(codingSharing)){
            throw new BusinessException("THERE IS NULL","TRY AGAGIN");
        }

        // 3. 浏览量+1
        com.haust.common.domain.po.CodingSharing codingSharing1 = new com.haust.common.domain.po.CodingSharing();
        codingSharing1.setClickNumber(codingSharing.getClickNumber()+1);
        codingSharing1.setId(id);
        codingSharingMapper.update(codingSharing1);
        return codingSharing;
    }

    @Override
    public PageVO<CodingSharingVo> page(PageDTO pageDTO) {
        // 0.设置分页参数
        PageHelper.startPage(pageDTO.getPage(),pageDTO.getPageSize());
        // 1.判断参数是否为空
        if(BeanUtil.isEmpty(pageDTO)){
            throw new BusinessException("ITEM IS NULL ","TRY AGAGIN");
        }
        // 1.1 设置用户查看权限
        // 赋予已通过的权限
        pageDTO.setStatus(1);
        // 2.开始分页查询
        List<CodingSharingVo> list = codingSharingMapper.page(pageDTO);
        // 3.封装使用 PageInfo 获取分页信息
        PageInfo<CodingSharingVo> pageInfo = new PageInfo<>(list);
        // 4. 封装数据
        PageVO<CodingSharingVo> pageVO = new PageVO<>();
        pageVO.setData(pageInfo.getList());     // 设置当前内容
        pageVO.setPage(pageInfo.getPageNum()); // 设置当前页码
        pageVO.setPageSize(pageInfo.getPageSize()); // 设置每页条数
        pageVO.setTotal((int) pageInfo.getTotal()); // 设置总条数
        return pageVO;
    }

    @Override
    public PageVO<CodingSharingVo> pageByAdmin(PageDTO pageDTO) {
        // 0.设置分页参数
        PageHelper.startPage(pageDTO.getPage(),pageDTO.getPageSize());
        // 1. 判断当前集合是否为null
        if(BeanUtil.isEmpty(pageDTO)){
            throw new BusinessException("ITEM IS NULL ","TRY AGAGIN");
        }
        // 2.开始分页查询
        List<CodingSharingVo> list = codingSharingMapper.page(pageDTO);
        // 3.封装使用 PageInfo 获取分页信息
        PageInfo<CodingSharingVo> pageInfo = new PageInfo<>(list);
        // 4. 封装数据
        PageVO<CodingSharingVo> pageVO = new PageVO<>();
        pageVO.setData(pageInfo.getList());     // 设置当前内容
        pageVO.setPage(pageInfo.getPageNum()); // 设置当前页码
        pageVO.setPageSize(pageInfo.getPageSize()); // 设置每页条数
        pageVO.setTotal((int) pageInfo.getTotal()); // 设置总条数
        return pageVO;
    }

    @Override
    public void permit(Long id, Integer status) {
        if (id == null) {
            throw new BusinessException("ID cannot be null","TRY AGAIN");
        }
        if (status == null) {
            throw new BusinessException("STATUS cannot be null","TRY AGAIN");
        }
        codingSharingMapper.permitInfo(id,status);
    }

    @Override
    public void delete(Long id) {

        if(id == null){
            throw new BusinessException("The Id is null","try again");
        }
        codingSharingMapper.delete(id);
    }

    @Override
    public PageVO<CodingSharing> pageMyInfo(PageDTO pageDTO) {
        // 0.设置分页参数
        PageHelper.startPage(pageDTO.getPage(),pageDTO.getPageSize());
        // 1. 判断当前集合是否为null
        if(BeanUtil.isEmpty(pageDTO)){
            throw new BusinessException("ITEM IS NULL ","TRY AGAGIN");
        }
        // 2.开始分页查询
        Long userId = BaseContext.getId();
        List<CodingSharing> list = codingSharingMapper.pageMyInfo(pageDTO,userId);
        // 3.封装使用 PageInfo 获取分页信息
        PageInfo<CodingSharing> pageInfo = new PageInfo<>(list);
        // 4. 封装数据
        PageVO<CodingSharing> pageVO = new PageVO<>();
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
    @SensitiveMonitor(ContentType.sharing)
    @Override
    public void modify(CodingSharingDTO codingSharingDTO) {
        com.haust.common.domain.po.CodingSharing codingSharing = new com.haust.common.domain.po.CodingSharing();
        Long userId = BaseContext.getId();
        codingSharing.setUserId(userId);
        BeanUtils.copyProperties(codingSharingDTO,codingSharing);
        codingSharing.setStatus(0);
        codingSharingMapper.update(codingSharing);
    }
}
