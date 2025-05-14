package com.phm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phm.common.Result;
import com.phm.model.entity.MedicalItem;
import com.phm.security.model.RoleConst;
import com.phm.service.IMedicalItemService;
import com.phm.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/medical-item")
public class MedicalItemController {
    @Autowired
    private IMedicalItemService medicalItemService;

    /**
     * 分页查询（支持项目名称、分类、适用品种模糊搜索）
     */
    @GetMapping("/page")
    public IPage<MedicalItem> page(@RequestParam int numPage, @RequestParam int pageSize, @RequestParam(required = false) String name, @RequestParam(required = false) String category, @RequestParam(required = false) String workedVariety) {
        return medicalItemService.pageQuery(numPage, pageSize, name, category, workedVariety);
    }

    /**
     * 根据ID查询单个项目
     */
    @GetMapping("/{id}")
    public MedicalItem getById(@PathVariable Long id) {
        return medicalItemService.getById(id);
    }

    /**
     * 添加医疗项目（需经理权限）
     */
    @PostMapping
    @Secured(RoleConst.MANAGER)
    public Result save(@RequestBody MedicalItem medicalItem) {
        // 校验必填字段
        if (StrUtil.isWhite(medicalItem.getName())) {
            return Result.error("项目名称不能为空");
        }
        if (medicalItem.getPrice() == null || medicalItem.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return Result.error("价格不能为空且不能为负数");
        }
        // 校验名称唯一性
        if (medicalItemService.lambdaQuery().eq(MedicalItem::getName, medicalItem.getName()).exists()) {
            return Result.error("项目名称已存在");
        }
        boolean success = medicalItemService.save(medicalItem);
        return Result.choice("添加", success);
    }

    /**
     * 更新医疗项目（需经理权限）
     */
    @PutMapping
    @Secured(RoleConst.MANAGER)
    public Result update(@RequestBody MedicalItem medicalItem) {
        if (medicalItem.getId() == null) {
            return Result.error("项目ID不能为空");
        }
        if (StrUtil.isWhite(medicalItem.getName())) {
            return Result.error("项目名称不能为空");
        }
        if (medicalItem.getPrice() == null || medicalItem.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return Result.error("价格不能为空且不能为负数");
        }
        // 校验名称唯一性（排除当前ID）
        if (medicalItemService.lambdaQuery().ne(MedicalItem::getId, medicalItem.getId()).eq(MedicalItem::getName, medicalItem.getName()).exists()) {
            return Result.error("项目名称已存在");
        }
        boolean success = medicalItemService.updateById(medicalItem);
        return Result.choice("更新", success);
    }

    /**
     * 删除医疗项目（需经理权限）
     */
    @DeleteMapping("/{id}")
    @Secured(RoleConst.MANAGER)
    public Result delete(@PathVariable Long id) {
        boolean success = medicalItemService.removeById(id);
        return Result.choice("删除", success);
    }

    /**
     * 删除医疗项目（需经理权限）
     */
    @DeleteMapping("/batch")
    @Secured(RoleConst.MANAGER)
    public Result batchDelete(@RequestBody List<Long> ids) {
        return Result.choice("删除", medicalItemService.removeByIds(ids));
    }
}
