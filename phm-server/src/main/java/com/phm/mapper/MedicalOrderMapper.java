package com.phm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phm.model.entity.MedicalOrder;
import com.phm.model.vo.MedicalOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MedicalOrderMapper extends BaseMapper<MedicalOrder> {
    /**
     * 分页联查（医疗项目+宠物+用户）
     */
    IPage<MedicalOrderVO> pageQuery(Page<MedicalOrderVO> page, @Param("medicalItemName") String medicalItemName,  // 医疗项目名（模糊）
                                    @Param("petName") String petName,                  // 宠物名（模糊）
                                    @Param("userName") String userName                  // 用户名（模糊）
    );

    /**
     * 单条联查（根据订单ID）
     */
    MedicalOrderVO getVOById(@Param("id") Long id);
}