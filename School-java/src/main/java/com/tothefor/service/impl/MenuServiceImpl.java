package com.tothefor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tothefor.mapper.MenuMapper;
import com.tothefor.mapper.UserMapper;
import com.tothefor.pojo.entity.Menu;
import com.tothefor.pojo.entity.User;
import com.tothefor.service.MenuService;
import com.tothefor.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author DragonOne
 * @Date 2022/3/10 15:43
 * @墨水记忆 www.tothefor.com
 */
@Service("MenuServiceImpl")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
}
