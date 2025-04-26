package com.phm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phm.common.Result;
import com.phm.mapper.DepartmentMapper;
import com.phm.model.dto.DepartmentDto;
import com.phm.model.entity.Department;
import com.phm.model.entity.Doctor;
import com.phm.model.dto.NameDo;
import com.phm.service.IDepartmentService;
import com.phm.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author 高晓文
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    private final IDoctorService doctorService;

    @Autowired
    public DepartmentServiceImpl(IDoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    public Result deleteById(Integer departmentId) {
        if (doctorService.lambdaQuery().eq(Doctor::getDepartmentId, departmentId).count() > 0)
            return Result.error("当前科室下有人");
        return Result.choice("删除单个", this.lambdaUpdate()
                .eq(Department::getDepartmentId, departmentId)
                .set(Department::getIsDel, System.currentTimeMillis())
                .update());
    }

    @Override
    public Result deleteByIds(List<Integer> idGroup) {
        if (idGroup.size() < 1) return Result.error("请选择数据"); // IN语句中没有数据会报错
        if (doctorService.lambdaQuery().in(Doctor::getDepartmentId, idGroup).count() > 0) {
            if (idGroup.size() == 1) return Result.error("所选科室有人");
            else return Result.error("部分所选科室有人");
        }
        return Result.choice("删除多个", lambdaUpdate()
                .in(Department::getDepartmentId, idGroup)
                .set(Department::getIsDel, System.currentTimeMillis())
                .update());
    }

    @Override
    public IPage<DepartmentDto> selectByPage(String departmentName, int numPage, int pageSize) {
        return baseMapper.selectDepartmentPage(new Page<>(numPage, pageSize), departmentName);
    }

    @Override
    public List<NameDo> selectName() {
        return baseMapper.selectName();
    }

    @Override
    public List<NameDo> selectNameHaveDoctor(Integer departmentId) {
        return baseMapper.selectNameHaveDoctor(departmentId);
    }

    @Override
    public boolean updateDepart(Department department) {
        return baseMapper.updateDepart(department);
    }
}
