package com.phm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phm.model.dto.NameDo;
import com.phm.model.entity.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 高晓文
 */
@Mapper
public interface ClientMapper extends BaseMapper<Client> {
    IPage<Client> selectClientPage(IPage<Client> page, String clientName);

    @Select("SELECT client_id as roleId, client_name as roleName FROM client WHERE is_del=0")
    List<NameDo> selectName();

    /**
     * 【子查询】名下有宠物的用户，带上当前用户
     *
     * @param clientId 当前客户ID
     * @return 客户名称+ID
     */
    List<NameDo> selectNameHavePet(Integer clientId);

    /**
     * 登录控制器：注册时确定当前用户名独一无二
     *
     * @param username 用户名
     * @return 数据条数
     */
    @Select("select count(*) from client where is_del=0 and client_username=#{username}")
    int isExist(@Param("username") String username);

    /**
     * 用户修改自己的资料
     *
     * @param client 用户信息
     * @return 修改成功
     */
    boolean updateSelfById(Client client);
}
