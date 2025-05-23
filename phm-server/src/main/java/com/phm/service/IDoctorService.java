package com.phm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phm.model.dto.DoctorDto;
import com.phm.model.entity.Doctor;
import com.phm.model.dto.NameDo;

import java.util.List;

/**
 * <p>
 * 医生表，外键部门 服务类
 * </p>
 *
 * @author 高晓文
 */
public interface IDoctorService extends IService<Doctor> {
    /**
     * 单个逻辑删除
     *
     * @param doctorId 医生id
     * @return 是否删除成功
     */
    boolean deleteById(Integer doctorId);

    /**
     * 批量逻辑删除
     *
     * @param idGroup 一组id
     * @return 是否批量删除成功
     */
    boolean deleteByIds(List<Integer> idGroup);

    /**
     * 分页查询
     *
     * @param doctorName     医生姓名，记得加模糊判别条件
     * @param departmentName 科室名，同上
     * @param numPage        当前页码
     * @param pageSize       当前一页的最大条数
     * @return 数据的总长度，与分页查询结果
     */
    IPage<DoctorDto> selectByPage(String doctorName, String departmentName, int numPage, int pageSize);

    /**
     * 根据id查询医生
     *
     * @param doctorId 医生id
     * @return 医生信息以及科室名
     */
    DoctorDto selectById(Integer doctorId);

    /**
     * 查询名字与对应id
     * <p>2023-9-1补充：虽然其它查名字的接口都带了当前角色。</p>
     * <p>这是为了防止当前角色被删，然后查询其相关信息只有一个id的情况。</p>
     * <p>但是这里不这样，因为人被删了，与之相关的应该被清除，而不是继续使用。</p>
     *
     * @return 相应角色名字与id
     */
    List<NameDo> selectName();

    /**
     * 管理员：在添加挂号单时，通过科室查询医生
     *
     * @param departmentId 科室id
     * @return 医生姓名与id
     */
    List<NameDo> selectNameByDepartment(Integer departmentId);

    /**
     * 科室管理：查看一个科室下面的所有医生
     *
     * @param departmentId 科室id
     * @return 对应科室的所有医生信息
     */
    List<Doctor> selectByDepartmentId(Integer departmentId);

    /**
     * 添加医生，关于加密与默认值
     */
    boolean addDoctor(Doctor doctor);

    /**
     * 医生更新自己的个人资料
     *
     * @param doctor 可以修改的五项信息与医生ID
     * @return 修改成功
     */
    boolean updateSelf(Doctor doctor);
}
