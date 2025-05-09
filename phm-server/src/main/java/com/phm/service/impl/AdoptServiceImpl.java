package com.phm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phm.common.Result;
import com.phm.mapper.AdoptMapper;
import com.phm.model.dto.AdoptDto;
import com.phm.model.entity.Adopt;
import com.phm.model.vo.AdoptVo;
import com.phm.service.IAdoptService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 领养宠物订单 服务实现类
 * </p>
 *
 * @author 高晓文
 */
@Service
public class AdoptServiceImpl extends ServiceImpl<AdoptMapper, Adopt> implements IAdoptService {

    @Override
    public boolean deleteById(Integer adoptId) {
        return this.lambdaUpdate()
                .eq(Adopt::getAdoptId, adoptId)
                .set(Adopt::getIsDel, System.currentTimeMillis())
                .update();
    }

    @Override
    public boolean deleteByIds(List<Integer> idGroup) {
        return this.lambdaUpdate()
                .in(Adopt::getAdoptId, idGroup)
                .set(Adopt::getIsDel, System.currentTimeMillis())
                .update();
    }

    @Override
    public IPage<AdoptDto> selectByPage(String clientName, String petName, int numPage, int pageSize) {
        return baseMapper.selectAdoptPage(new Page<>(numPage, pageSize), clientName, petName);
    }

    @Override
    public Result adoptAdopt(AdoptVo adoptVo) {
        // 查看是否有主
        if (baseMapper.petMaster(adoptVo.adoptId()) > 0) return Result.error("此物有主");
        // 无主则判断是否正常操作
        if (adoptVo.isAdopt() == null) return Result.error("你不对劲");
        // 正常操作则进行同意or拒绝
        if (adoptVo.isAdopt()) {
            String petInfo = "它有了温暖的家，感恩";
            return Result.choice("领养", baseMapper.adoptAdopt(adoptVo.adoptId(), petInfo));
        } else {
            return Result.choice("拒绝", baseMapper.refuse(adoptVo.adoptId()));
        }
    }

    @Override
    public int sureInAdopt(Integer petId, Integer clientId) {
        return baseMapper.sureInAdopt(petId, clientId);
    }
}
