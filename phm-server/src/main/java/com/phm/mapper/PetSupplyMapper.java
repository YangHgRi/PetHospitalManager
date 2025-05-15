package com.phm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phm.model.entity.PetSupply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PetSupplyMapper extends BaseMapper<PetSupply> {

    /**
     * 分页查询（支持名字模糊+时间精准）
     */
    IPage<PetSupply> selectSupplyPage(
        IPage<PetSupply> page,
        @Param("name") String name,
        @Param("shelfTime") String shelfTime
    );
}
