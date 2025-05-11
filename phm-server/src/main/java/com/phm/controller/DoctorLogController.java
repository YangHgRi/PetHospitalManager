package com.phm.controller;

import com.phm.common.Result;
import com.phm.model.dto.AppointmentDto2;
import com.phm.model.dto.DoctorDto;
import com.phm.model.entity.Appointment;
import com.phm.model.entity.Doctor;
import com.phm.model.vo.DealAppointVo;
import com.phm.security.model.RoleConst;
import com.phm.service.IAppointmentService;
import com.phm.service.IDoctorService;
import com.phm.util.StrUtil;
import com.phm.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生：已经登录
 */
@RestController
@RequestMapping("/doctorLog")
@Secured(RoleConst.LOSER)
public class DoctorLogController {
    private final IDoctorService doctorService;
    private final IAppointmentService appointService;

    @Autowired
    public DoctorLogController(IDoctorService doctorService, IAppointmentService appointService) {
        this.doctorService = doctorService;
        this.appointService = appointService;
    }

    /**
     * 医生：查询当前登录的医生信息
     */
    @GetMapping
    public DoctorDto getOneLogin() {
        if (UserUtil.getUser() instanceof Doctor doctor) {
            return doctorService.selectById(doctor.getDoctorId());
        } else return null;
    }

    /**
     * 医生：修改简历
     */
    @PutMapping
    public Result update(@RequestBody Doctor doctor) {
        if (StrUtil.isWhite(doctor.getDoctorName())) return Result.error("姓名不能为空");
        if (UserUtil.getUser() instanceof Doctor doctorLog) {
            doctor.setDoctorId(doctorLog.getDoctorId());
            return Result.choice("修改", doctorService.updateSelf(doctor));
        } else return null;
    }

    /**
     * 医生：查询自己的订单
     */
    @GetMapping("/appoint")
    public List<AppointmentDto2> getAppoint() {
        if (UserUtil.getUser() instanceof Doctor doctor) {
            return appointService.getDoctorLog(doctor.getDoctorId());
        } else return null;
    }

    /**
     * 医生：添加挂号
     */
    @PostMapping("/appoint")
    public Result addAppoint(@RequestBody Appointment appointment) {
        if (UserUtil.getUser() instanceof Doctor doctorLog) {
            appointment.setDoctorId(doctorLog.getDoctorId());
            appointment.setDepartmentId(doctorLog.getDepartmentId());
            return Result.choice("添加", appointService.save(appointment));
        } else return Result.error("未获取登录信息");
    }

    /**
     * 医生：处理挂号
     */
    @PutMapping("/appoint/deal")
    public Result dealAppoint(@RequestBody DealAppointVo dealAppointVo) {
        return Result.choice("处理", appointService.lambdaUpdate().eq(Appointment::getAppointmentId, dealAppointVo.appointId()).set(Appointment::getIsSuc, dealAppointVo.isSuc()).update());
    }
}
