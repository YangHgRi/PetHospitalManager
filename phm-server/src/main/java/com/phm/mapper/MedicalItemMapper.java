package com.phm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phm.model.entity.MedicalItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MedicalItemMapper extends BaseMapper<MedicalItem> {
    // 基础CRUD已由BaseMapper提供，无需额外定义
}