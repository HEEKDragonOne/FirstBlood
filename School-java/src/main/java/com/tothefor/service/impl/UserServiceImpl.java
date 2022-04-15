package com.tothefor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tothefor.mapper.MenuMapper;
import com.tothefor.mapper.RoleMenuMapper;
import com.tothefor.mapper.UserMapper;
import com.tothefor.pojo.dto.LoginDTO;
import com.tothefor.pojo.entity.Menu;
import com.tothefor.pojo.entity.RoleMenu;
import com.tothefor.pojo.entity.User;
import com.tothefor.resultR.R;
import com.tothefor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author DragonOne
 * @Date 2022/3/10 15:43
 * @墨水记忆 www.tothefor.com
 */
@Service("UserServiceImpl")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 通过用户名查找菜单权限
     * @param username
     * @return
     */
    @Override
    public List<Menu> menuByName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userMapper.selectOne(queryWrapper);
        //查找角色
        Long roleId = user.getRoleId();
        QueryWrapper<RoleMenu> qw = new QueryWrapper<>();
        qw.eq("role_id",roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(qw);

        //查找角色对应的菜单
        List<Menu> list = new ArrayList<>();
        for(RoleMenu it : roleMenus){
            Long menuId = it.getMenuId();
            Menu menu = menuMapper.selectById(menuId);
            list.add(menu);
        }
//            存储角色有的的菜单id
        Set<Long> set = new HashSet<>();

        for(Menu it: list){
            set.add(it.getId());
            if(it.getPid()!=null)set.add(it.getPid());
        }
        //            通过id找到菜单并加入
        List<Menu> listall = new ArrayList<>();
        for(Long it : set){
            Menu menu = menuMapper.selectById(it);
            listall.add(menu);
        }

        List<Menu> parNode = listall.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        for( Menu it : parNode){
            it.setChildren(listall.stream().filter(m->it.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parNode;
    }
}
