package com.phm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phm.common.Result;
import com.phm.model.dto.PetDto;
import com.phm.model.entity.Client;
import com.phm.model.entity.Pet;
import com.phm.model.dto.NameDo;
import com.phm.security.model.RoleConst;
import com.phm.service.IPetService;
import com.phm.util.StrUtil;
import com.phm.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 宠物信息表，外键用户表 前端控制器
 * </p>
 *
 * @author 高晓文
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    private final IPetService petService;

    @Autowired
    public PetController(IPetService petService) {
        this.petService = petService;
    }

    /**
     * 添加寄养、挂号单时，查询主人对应的宠物
     */
    @GetMapping("/client")
    public List<NameDo> getByClient(Integer clientId, Integer petId) {
        return petService.selectByClient(clientId, petId);
    }

    /**
     * 用户个人中心：查询名下宠物
     *
     * @return 宠物列表
     */
    @GetMapping("/clientOne")
    public List<Pet> getByClientOne() {
        if (UserUtil.getUser() instanceof Client client) {
            return petService.lambdaQuery()
                    .eq(Pet::getClientId, client.getClientId())
                    .list();
        } else return null;
    }

    /**
     * 管理员：添加领养订单时，查询无主宠物
     *
     * @return 无主宠物的姓名与ID
     */
    @GetMapping("/noClient")
    public List<NameDo> getNoClient() {
        return petService.selectNoClient();
    }

    @GetMapping("/page")
    public IPage<PetDto> getPage(int numPage, int pageSize, String petName, String clientName) {
        return petService.selectByPage(petName, clientName, numPage, pageSize);
    }

    /**
     * 用户前台查询待领养宠物
     *
     * @return 待领养宠物列表
     */
    @GetMapping("/four")
    public IPage<Pet> getFour(int numPage, int pageSize) {
        return petService.selectFour(numPage, pageSize);
    }

    /**
     * 用户查看待领养宠物的详情
     *
     * @param petId 待领养宠物的ID
     * @return 宠物信息
     */
    @GetMapping("/one")
    public Pet getOne(Integer petId) {
        return petService.getById(petId);
    }

    @PostMapping
    @Secured(RoleConst.NURSE)
    public Result save(@RequestBody Pet pet) {
        if (StrUtil.isWhite(pet.getPetName())) return Result.error("姓名不能为空");
        if (StrUtil.isWhite(pet.getPetVariety())) return Result.error("品种不能为空");
        return Result.choice("添加", petService.addPet(pet));
    }

    @DeleteMapping("/{id}")
    @Secured(RoleConst.MANAGER)
    public Result delete(@PathVariable Integer id) {
        return Result.choice("删除单个", petService.deleteById(id));
    }

    @DeleteMapping("/batch/{ids}")
    @Secured(RoleConst.MANAGER)
    public Result deleteGroup(@PathVariable List<Integer> ids) {
        return Result.choice("删除多个", petService.deleteByIds(ids));
    }

    @PutMapping
    @Secured(RoleConst.MANAGER)
    public Result update(@RequestBody Pet pet) {
        if (StrUtil.isWhite(pet.getPetName())) return Result.error("姓名不能为空");
        if (StrUtil.isWhite(pet.getPetVariety())) return Result.error("品种不能为空");
        return Result.choice("修改", petService.updatePet(pet));
    }
}
