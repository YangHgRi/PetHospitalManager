package com.phm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phm.common.Result;
import com.phm.model.dto.FosterDto;
import com.phm.model.dto.FosterPetDto;
import com.phm.model.entity.Foster;
import com.phm.security.model.RoleConst;
import com.phm.service.IFosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 寄养表 前端控制器
 * </p>
 *
 * @author 高晓文
 */
@RestController
@RequestMapping("/foster")
public class FosterController {
    private final IFosterService fosterService;

    @Autowired
    public FosterController(IFosterService fosterService) {
        this.fosterService = fosterService;
    }

    @GetMapping("/page")
    public IPage<FosterDto> getPage(int numPage, int pageSize, String clientName, String petName) {
        return fosterService.selectByPage(clientName, petName, numPage, pageSize);
    }

    /**
     * 用户前台：查看被寄养的宠物
     *
     * @return 被寄养宠物的卡片信息
     */
    @GetMapping("/pet")
    public IPage<FosterPetDto> fosterPet(int pageSize) {
        return fosterService.fosterPet(pageSize);
    }

    @PostMapping
    @Secured(RoleConst.NURSE)
    public Result save(@RequestBody Foster foster) {
        if (foster.getFosterMoney() <= 0) return Result.error("金额必须大于零");
        if (fosterService.haveFoster(foster.getPetId()) > 0) return Result.error("当前宠物已在寄养中");
        return Result.choice("添加", fosterService.save(foster));
    }

    @DeleteMapping("/{id}")
    @Secured(RoleConst.MANAGER)
    public Result delete(@PathVariable Integer id) {
        return Result.choice("删除单个", fosterService.deleteById(id));
    }

    @DeleteMapping("/batch/{ids}")
    @Secured(RoleConst.MANAGER)
    public Result deleteGroup(@PathVariable List<Integer> ids) {
        return Result.choice("删除多个", fosterService.deleteByIds(ids));
    }

    @PutMapping
    @Secured(RoleConst.MANAGER)
    public Result update(@RequestBody Foster foster) {
        if (foster.getFosterMoney() <= 0) return Result.error("金额必须大于零");
        return Result.choice("修改", fosterService.updateFoster(foster));
    }
}
