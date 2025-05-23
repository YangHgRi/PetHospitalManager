package com.phm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phm.model.dto.DepartmentDto;
import com.phm.model.entity.Department;
import com.phm.model.dto.NameDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author 高晓文
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    IPage<DepartmentDto> selectDepartmentPage(IPage<Department> page, String departmentName);

    @Select("SELECT department_id as roleId, department_name as roleName FROM department WHERE is_del=0")
    List<NameDo> selectName();

    /**
     * 【内联查询】名下有医生的科室，带上当前科室
     *
     * @param departmentId 当前科室ID
     * @return 科室名称+ID
     */
    List<NameDo> selectNameHaveDoctor(Integer departmentId);

    /**
     * 更新科室，让主任可以为空
     *
     * @param department 科室信息
     * @return 更新成功
     */
    boolean updateDepart(Department department);
}
