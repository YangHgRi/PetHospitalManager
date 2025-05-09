package com.phm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phm.common.PhotoEnum;
import com.phm.mapper.DoctorMapper;
import com.phm.model.dto.DoctorDto;
import com.phm.model.entity.Doctor;
import com.phm.model.dto.NameDo;
import com.phm.service.IDoctorService;
import com.phm.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 医生表，外键部门 服务实现类
 * </p>
 *
 * @author 高晓文
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements IDoctorService {
    @Override
    public boolean deleteById(Integer doctorId) {
        return this.lambdaUpdate()
                .eq(Doctor::getDoctorId, doctorId)
                .set(Doctor::getIsDel, System.currentTimeMillis())
                .update();
    }

    @Override
    public boolean deleteByIds(List<Integer> idGroup) {
        return this.lambdaUpdate()
                .in(Doctor::getDoctorId, idGroup)
                .set(Doctor::getIsDel, System.currentTimeMillis())
                .update();
    }

    @Override
    public IPage<DoctorDto> selectByPage(String doctorName, String departmentName, int numPage, int pageSize) {
        return baseMapper.selectDoctorPage(new Page<>(numPage, pageSize), doctorName, departmentName);
    }

    @Override
    public DoctorDto selectById(Integer doctorId) {
        return baseMapper.selectOneById(doctorId);
    }

    @Override
    public List<NameDo> selectName() {
        return baseMapper.selectName();
    }

    @Override
    public List<NameDo> selectNameByDepartment(Integer departmentId) {
        return baseMapper.selectNameByDepartment(departmentId);
    }

    @Override
    public List<Doctor> selectByDepartmentId(Integer departmentId) {
        return baseMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public boolean addDoctor(Doctor doctor) {
        if (doctor.getAuthLv() == null) doctor.setAuthLv(0);
        if (StrUtil.isWhite(doctor.getDoctorInfo())) doctor.setDoctorInfo("暂时没有简介");
        if (StrUtil.isWhite(doctor.getDoctorPhoto())) doctor.setDoctorPhoto(PhotoEnum.DOCTOR.getPhotoName());
        if (StrUtil.isWhite(doctor.getDoctorPassword())) doctor.setDoctorPassword("123456");
        doctor.setDoctorPassword(StrUtil.tranPwd(doctor.getDoctorPassword()));
        return save(doctor);
    }

    @Override
    public boolean updateSelf(Doctor doctor) {
        return baseMapper.updateSelf(doctor);
    }
}
