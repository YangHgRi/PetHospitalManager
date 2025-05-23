package com.phm.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serial;

/**
 * <p>
 * 医生表，外键部门
 * </p>
 *
 * @author 高晓文
 */
public class Doctor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 医生id
     */
    @TableId(value = "doctor_id", type = IdType.AUTO)
    private Integer doctorId;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 医生帐号
     */
    private String username;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 医生性别，1男0女
     */
    private Boolean doctorGender;

    /**
     * 医生生日
     */
    private LocalDate doctorAge;

    /**
     * 医生照片名称
     */
    private String doctorPhoto;

    /**
     * 医生联系方式
     */
    private String doctorTel;

    /**
     * 权限等级
     */
    private Integer authLv;

    /**
     * 医生简介
     */
    private String doctorInfo;

    /**
     * 医生密码
     */
    private String doctorPassword;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除，默认0，填充删除日期
     */
    private Long isDel;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Boolean getDoctorGender() {
        return doctorGender;
    }

    public void setDoctorGender(Boolean doctorGender) {
        this.doctorGender = doctorGender;
    }

    public LocalDate getDoctorAge() {
        return doctorAge;
    }

    public void setDoctorAge(LocalDate doctorAge) {
        this.doctorAge = doctorAge;
    }

    public String getDoctorPhoto() {
        return doctorPhoto;
    }

    public void setDoctorPhoto(String doctorPhoto) {
        this.doctorPhoto = doctorPhoto;
    }

    public String getDoctorTel() {
        return doctorTel;
    }

    public void setDoctorTel(String doctorTel) {
        this.doctorTel = doctorTel;
    }

    public Integer getAuthLv() {
        return authLv;
    }

    public void setAuthLv(Integer authLv) {
        this.authLv = authLv;
    }

    public String getDoctorInfo() {
        return doctorInfo;
    }

    public void setDoctorInfo(String doctorInfo) {
        this.doctorInfo = doctorInfo;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getIsDel() {
        return isDel;
    }

    public void setIsDel(Long isDel) {
        this.isDel = isDel;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId = " + doctorId +
                ", departmentId = " + departmentId +
                ", username = " + username +
                ", doctorName = " + doctorName +
                ", doctorGender = " + doctorGender +
                ", doctorAge = " + doctorAge +
                ", doctorPhoto = " + doctorPhoto +
                ", doctorTel = " + doctorTel +
                ", authLv = " + authLv +
                ", doctorInfo = " + doctorInfo +
                ", doctorPassword = " + doctorPassword +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", isDel = " + isDel +
                "}";
    }
}
