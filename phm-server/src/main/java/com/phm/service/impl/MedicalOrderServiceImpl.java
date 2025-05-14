package com.phm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phm.mapper.MedicalOrderMapper;
import com.phm.model.entity.MedicalOrder;
import com.phm.model.vo.MedicalOrderVO;
import com.phm.service.IMedicalOrderService;
import com.phm.util.StrUtil;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MedicalOrderServiceImpl extends ServiceImpl<MedicalOrderMapper, MedicalOrder> implements IMedicalOrderService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    @Override
    public IPage<MedicalOrderVO> pageQuery(int numPage, int pageSize, String medicalItemName, String petName, String userName) {
        // 处理空参数（转为null避免SQL错误）
        String miName = StrUtil.isWhite(medicalItemName) ? null : medicalItemName;
        String pName = StrUtil.isWhite(petName) ? null : petName;
        String uName = StrUtil.isWhite(userName) ? null : userName;

        Page<MedicalOrderVO> page = new Page<>(numPage, pageSize);
        IPage<MedicalOrderVO> medicalOrderVOIPage = baseMapper.pageQuery(page, miName, pName, uName);
        List<MedicalOrderVO> records = medicalOrderVOIPage.getRecords();
        records.forEach(record -> {
            record.setFormattedCreatedAt(formatter.format(record.getCreatedAt().toInstant()));
        });
        medicalOrderVOIPage.setRecords(records);
        return medicalOrderVOIPage;
    }

    @Override
    public MedicalOrderVO getVOById(Long id) {
        return baseMapper.getVOById(id);
    }
}