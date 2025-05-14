package com.phm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phm.model.entity.MedicalOrder;
import com.phm.model.vo.MedicalOrderVO;

public interface IMedicalOrderService extends IService<MedicalOrder> {
    /**
     * 分页联查（带模糊条件）
     */
    IPage<MedicalOrderVO> pageQuery(int numPage, int pageSize, String medicalItemName, String petName, String userName);

    /**
     * 单条联查（根据订单ID）
     */
    MedicalOrderVO getVOById(Long id);
}