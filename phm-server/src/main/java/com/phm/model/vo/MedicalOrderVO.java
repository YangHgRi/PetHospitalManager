package com.phm.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MedicalOrderVO {
    private Long id;                  // 订单ID
    private Long itemId;
    private Long petId;
    private Long userId;
    private BigDecimal itemPrice;
    private String medicalItemName;   // 医疗项目名称（联查medical_item.name）
    private String petName;           // 宠物名称（联查pet.pet_name）
    private String userName;          // 用户名（联查client.client_name）
    private Date createdAt;           // 创建时间
    private String formattedCreatedAt;
    // 扩展字段（可选）：医疗项目价格、宠物品种等
}
