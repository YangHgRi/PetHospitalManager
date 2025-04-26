package com.phm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phm.common.PhotoEnum;
import com.phm.mapper.PetMapper;
import com.phm.model.dto.PetDto;
import com.phm.model.entity.Pet;
import com.phm.model.dto.NameDo;
import com.phm.service.IPetService;
import com.phm.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 宠物信息表，外键用户表 服务实现类
 * </p>
 *
 * @author 高晓文
 */
@Service
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements IPetService {

    @Override
    public boolean deleteById(Integer petId) {
        return this.lambdaUpdate()
                .eq(Pet::getPetId, petId)
                .set(Pet::getIsDel, System.currentTimeMillis())
                .update();
    }

    @Override
    public boolean deleteByIds(List<Integer> idGroup) {
        return this.lambdaUpdate()
                .in(Pet::getPetId, idGroup)
                .set(Pet::getIsDel, System.currentTimeMillis())
                .update();
    }

    @Override
    public IPage<PetDto> selectByPage(String petName, String clientName, int numPage, int pageSize) {
        return baseMapper.selectPetPage(new Page<>(numPage, pageSize), petName, clientName);
    }

    @Override
    public IPage<Pet> selectFour(int numPage, int pageSize) {
        return baseMapper.selectFour(new Page<>(numPage, pageSize));
    }

    @Override
    public boolean updatePet(Pet pet) {
        return baseMapper.updatePet(pet);
    }

    @Override
    public List<NameDo> selectName() {
        return baseMapper.selectName();
    }

    @Override
    public List<NameDo> selectByClient(Integer clientId, Integer petId) {
        return baseMapper.selectByClient(clientId, petId);
    }

    @Override
    public List<NameDo> selectNoClient() {
        return baseMapper.selectNoClient();
    }

    @Override
    public boolean addPet(Pet pet) {
        if (StrUtil.isWhite(pet.getPetPhoto())) pet.setPetPhoto(PhotoEnum.PET.getPhotoName());
        if (StrUtil.isWhite(pet.getPetInfo())) pet.setPetInfo("暂无近况");
        if (StrUtil.isWhite(pet.getPetStatus())) pet.setPetStatus("健康");
        return save(pet);
    }
}
