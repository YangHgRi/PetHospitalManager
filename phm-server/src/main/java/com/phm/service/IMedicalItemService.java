package com.phm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phm.model.entity.MedicalItem;

public interface IMedicalItemService extends IService<MedicalItem> {

    /**
     * 分页查询（带模糊条件）
     * @param numPage 当前页码
     * @param pageSize 每页大小
     * @param name 项目名称（模糊）
     * @param category 项目分类（模糊）
     * @param workedVariety 适用品种（模糊）
     * @return 分页结果
     */
    IPage<MedicalItem> pageQuery(int numPage, int pageSize, String name, String category, String workedVariety);
}
