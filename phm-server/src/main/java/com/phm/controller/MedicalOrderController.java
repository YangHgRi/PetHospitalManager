package com.phm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phm.common.Result;
import com.phm.model.entity.MedicalOrder;
import com.phm.model.vo.MedicalOrderVO;
import com.phm.service.IMedicalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-order")
public class MedicalOrderController {
    @Autowired
    private IMedicalOrderService medicalOrderService;

    /**
     * 分页查询（支持医疗项目名、宠物名、用户名模糊搜索）
     */
    @GetMapping("/page")
    public IPage<MedicalOrderVO> page(@RequestParam int numPage, @RequestParam int pageSize, @RequestParam(required = false) String medicalItemName, @RequestParam(required = false) String petName, @RequestParam(required = false) String userName) {
        return medicalOrderService.pageQuery(numPage, pageSize, medicalItemName, petName, userName);
    }

    /**
     * 单条查询（联查显示详细信息）
     */
    @GetMapping("/{id}")
    public MedicalOrderVO getById(@PathVariable Long id) {
        return medicalOrderService.getVOById(id);
    }

    /**
     * 添加医疗订单
     */
    @PostMapping
    public Result save(@RequestBody MedicalOrder medicalOrder) {
        // 校验必填字段
        if (medicalOrder.getItemId() == null) return Result.error("医疗项目ID不能为空");
        if (medicalOrder.getPetId() == null) return Result.error("宠物ID不能为空");
        if (medicalOrder.getUserId() == null) return Result.error("用户ID不能为空");

        boolean success = medicalOrderService.save(medicalOrder);
        return Result.choice("添加", success);
    }

    @PutMapping
    public Result update(@RequestBody MedicalOrder medicalOrder) {
        // 校验必填字段
        if (medicalOrder.getItemId() == null) return Result.error("医疗项目ID不能为空");
        if (medicalOrder.getPetId() == null) return Result.error("宠物ID不能为空");
        if (medicalOrder.getUserId() == null) return Result.error("用户ID不能为空");

        boolean success = medicalOrderService.updateById(medicalOrder);
        return Result.choice("更新", success);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch")
    public Result batchDelete(@RequestBody List<Long> ids) {
        if (ids.isEmpty()) return Result.error("删除ID列表不能为空");
        boolean success = medicalOrderService.removeByIds(ids);
        return Result.choice("批量删除", success);
    }

    /**
     * 单条删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        boolean success = medicalOrderService.removeById(id);
        return Result.choice("删除", success);
    }
}