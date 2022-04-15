package com.tothefor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tothefor.pojo.entity.Item;
import com.tothefor.pojo.entity.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author DragonOne
 * @Date 2022/3/10 16:01
 * @墨水记忆 www.tothefor.com
 */
@Service
public interface UserRoleService extends IService<UserRole> {
    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}
