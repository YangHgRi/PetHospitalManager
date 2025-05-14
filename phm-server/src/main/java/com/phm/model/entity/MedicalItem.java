package com.phm.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("medical_item")
public class MedicalItem {
    @TableId(type = IdType.AUTO)
    private Long id;          // 主键ID
    private String name;      // 项目名称
    private String category;  // 项目分类（如手术/检测等）
    private String workedVariety; // 适用品种（犬、猫等）
    private BigDecimal price; // 项目价格
    private String description; // 详细描述
}
