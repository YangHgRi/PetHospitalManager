package com.phm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phm.mapper.MedicalItemMapper;
import com.phm.model.entity.MedicalItem;
import com.phm.service.IMedicalItemService;
import com.phm.util.StrUtil;
import org.springframework.stereotype.Service;

@Service
public class MedicalItemServiceImpl extends ServiceImpl<MedicalItemMapper, MedicalItem> implements IMedicalItemService {

    @Override
    public IPage<MedicalItem> pageQuery(int numPage, int pageSize, String name, String category, String workedVariety) {
        // 构建分页对象
        Page<MedicalItem> page = new Page<>(numPage, pageSize);
        // 构建查询条件（模糊查询）
        LambdaQueryWrapper<MedicalItem> queryWrapper = new LambdaQueryWrapper<>();
        if (!StrUtil.isWhite(name)) {
            queryWrapper.like(MedicalItem::getName, name);
        }
        if (!StrUtil.isWhite(category)) {
            queryWrapper.like(MedicalItem::getCategory, category);
        }
        if (!StrUtil.isWhite(workedVariety)) {
            queryWrapper.like(MedicalItem::getWorkedVariety, workedVariety);
        }
        // 执行分页查询
        return page(page, queryWrapper);
    }
}
