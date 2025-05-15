package com.phm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phm.common.Result;
import com.phm.model.entity.PetSupply;
import com.phm.security.model.RoleConst;
import com.phm.service.IPetSupplyService;
import com.phm.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/pet-supply")
public class PetSupplyController {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final IPetSupplyService petSupplyService;

    @Autowired
    public PetSupplyController(IPetSupplyService petSupplyService) {
        this.petSupplyService = petSupplyService;
    }

    /**
     * 分页查询（支持名字模糊匹配+上架时间精准匹配）
     */
    @GetMapping("/page")
    public IPage<PetSupply> getPage(@RequestParam int numPage, @RequestParam int pageSize, @RequestParam(required = false) String name, @RequestParam(required = false) String shelfTime) {
        return petSupplyService.selectPage(numPage, pageSize, name, shelfTime);
    }

    /**
     * 新增宠物用品
     */
    @PostMapping
    @Secured(RoleConst.MANAGER)
    public Result save(@RequestBody PetSupply petSupply) {
        if (StrUtil.isWhite(petSupply.getName())) {
            return Result.error("用品名称不能为空");
        }
        String shelfTime = petSupply.getShelfTime();
        if (StrUtil.isWhite(shelfTime)) {
            shelfTime = formatter.format(LocalDateTime.now());
            petSupply.setShelfTime(shelfTime);
        }
        boolean save = petSupplyService.save(petSupply);
        return Result.choice("添加", save);
    }

    /**
     * 删除单个宠物用品（物理删除）
     */
    @DeleteMapping("/{id}")
    @Secured(RoleConst.MANAGER)
    public Result delete(@PathVariable Integer id) {
        boolean remove = petSupplyService.removeById(id);
        return Result.choice("删除单个", remove);
    }

    /**
     * 批量删除宠物用品（物理删除）
     */
    @DeleteMapping("/batch/{ids}")
    @Secured(RoleConst.MANAGER)
    public Result deleteBatch(@PathVariable List<Integer> ids) {
        boolean remove = petSupplyService.removeByIds(ids);
        return Result.choice("删除多个", remove);
    }

    /**
     * 修改宠物用品信息
     */
    @PutMapping
    @Secured(RoleConst.MANAGER)
    public Result update(@RequestBody PetSupply petSupply) {
        if (StrUtil.isWhite(petSupply.getName())) {
            return Result.error("用品名称不能为空");
        }
        boolean update = petSupplyService.updateById(petSupply);
        return Result.choice("修改", update);
    }
}
