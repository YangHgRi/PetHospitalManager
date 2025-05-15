package com.phm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phm.model.entity.PetSupply;

public interface IPetSupplyService extends IService<PetSupply> {

    /**
     * 分页查询（支持名字模糊+时间精准）
     */
    IPage<PetSupply> selectPage(int numPage, int pageSize, String name, String shelfTime);
}
