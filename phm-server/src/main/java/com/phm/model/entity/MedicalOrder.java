package com.phm.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("medical_order")
public class MedicalOrder {
    @TableId(type = IdType.AUTO)
    private Long id;          // 订单主键ID
    private Long itemId;      // 关联医疗项目ID（对应medical_item.id）
    private Long petId;       // 关联宠物ID（对应pet.pet_id）
    private Long userId;      // 关联用户ID（对应client.client_id）
    private Date createdAt;   // 创建时间（自动生成）
}
