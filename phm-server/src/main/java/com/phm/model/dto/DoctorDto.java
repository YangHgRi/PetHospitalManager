package com.phm.model.dto;

import com.phm.model.entity.Doctor;

public class DoctorDto extends Doctor {
    private String departmentName;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
