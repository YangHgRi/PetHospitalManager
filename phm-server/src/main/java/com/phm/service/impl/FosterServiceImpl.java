package com.phm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phm.mapper.FosterMapper;
import com.phm.model.dto.FosterDto;
import com.phm.model.dto.FosterPetDto;
import com.phm.model.entity.Foster;
import com.phm.service.IFosterService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 寄养表 服务实现类
 * </p>
 *
 * @author 高晓文
 */
@Service
public class FosterServiceImpl extends ServiceImpl<FosterMapper, Foster> implements IFosterService {

    @Override
    public boolean deleteById(Integer fosterId) {
        return this.lambdaUpdate()
                .eq(Foster::getFosterId, fosterId)
                .set(Foster::getIsDel, System.currentTimeMillis())
                .update();
    }

    @Override
    public boolean deleteByIds(List<Integer> idGroup) {
        return this.lambdaUpdate()
                .in(Foster::getFosterId, idGroup)
                .set(Foster::getIsDel, System.currentTimeMillis())
                .update();
    }

    @Override
    public IPage<FosterDto> selectByPage(String clientName, String petName, int numPage, int pageSize) {
        return baseMapper.selectFosterPage(new Page<>(numPage, pageSize), clientName, petName);
    }

    @Override
    public IPage<FosterPetDto> fosterPet(int pageSize) {
        return baseMapper.fosterPet(new Page<>(1, pageSize));
    }

    @Override
    public Integer haveFoster(Integer petId) {
        // SELECT COUNT(*) FROM foster WHERE is_del='0' AND pet_id=? AND foster_term>=?
        return Math.toIntExact(this.lambdaQuery()
                .eq(Foster::getPetId, petId)
                // ge:大于开始时间;le:小于等于结束时间
                .ge(Foster::getFosterTerm, LocalDate.now())
                .count());
    }

    @Override
    public boolean save(Foster entity) {
        String nowDate = new SimpleDateFormat("yyMMdd").format(new Date());
        entity.setFosterCode(1 + nowDate + (baseMapper.getMaxId() + 1));
        return super.save(entity);
    }

    @Override
    public boolean updateFoster(Foster foster) {
        return baseMapper.updateFoster(foster);
    }
}
