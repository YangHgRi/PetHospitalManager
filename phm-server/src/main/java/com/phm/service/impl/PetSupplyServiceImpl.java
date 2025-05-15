package com.phm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phm.mapper.PetSupplyMapper;
import com.phm.model.entity.PetSupply;
import com.phm.service.IPetSupplyService;
import org.springframework.stereotype.Service;

@Service
public class PetSupplyServiceImpl extends ServiceImpl<PetSupplyMapper, PetSupply> implements IPetSupplyService {

    @Override
    public IPage<PetSupply> selectPage(int numPage, int pageSize, String name, String shelfTime) {
        Page<PetSupply> page = new Page<>(numPage, pageSize);
        return baseMapper.selectSupplyPage(page, name, shelfTime);
    }
}
