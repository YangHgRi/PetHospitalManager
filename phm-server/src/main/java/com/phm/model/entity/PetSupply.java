package com.phm.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 宠物用品实体类
 */
@Data
@TableName("pet_supply")
public class PetSupply {
    @TableId(type = IdType.AUTO)
    private Integer id;         // 主键ID
    private String name;        // 用品名称
    private String description; // 用品介绍
    private BigDecimal price;  // 用品价格
    private String shelfTime; // 上架时间
    private String imageFilename;    // 图片文件名
}
