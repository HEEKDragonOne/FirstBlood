package com.tothefor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tothefor.mapper.ItemMapper;
import com.tothefor.mapper.RoleMenuMapper;
import com.tothefor.mapper.UserRoleMapper;
import com.tothefor.pojo.entity.Item;
import com.tothefor.pojo.entity.RoleMenu;
import com.tothefor.pojo.entity.UserRole;
import com.tothefor.service.ItemService;
import com.tothefor.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author DragonOne
 * @Date 2022/3/10 16:02
 * @墨水记忆 www.tothefor.com
 */
@Service("UserRoleServiceImpl")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {


    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleMenuServiceImpl roleMenuService;

    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {

        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        roleMenuMapper.delete(queryWrapper);

//        String sd = roleId.toString();
        for(Integer it: menuIds){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(Long.valueOf(roleId));
            roleMenu.setMenuId(Long.valueOf(it));
            roleMenuMapper.insert(roleMenu);

        }

    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
