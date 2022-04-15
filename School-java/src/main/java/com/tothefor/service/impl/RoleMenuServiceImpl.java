package com.tothefor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tothefor.mapper.MenuMapper;
import com.tothefor.mapper.RoleMenuMapper;
import com.tothefor.pojo.entity.Menu;
import com.tothefor.pojo.entity.RoleMenu;
import com.tothefor.service.MenuService;
import com.tothefor.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * @Author DragonOne
 * @Date 2022/3/10 15:43
 * @墨水记忆 www.tothefor.com
 */
@Service("RoleMenuServiceImpl")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
