package com.tothefor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tothefor.pojo.dto.LoginDTO;
import com.tothefor.pojo.entity.Item;
import com.tothefor.pojo.entity.Menu;
import com.tothefor.pojo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author DragonOne
 * @Date 2022/3/10 15:42
 * @墨水记忆 www.tothefor.com
 */

@Service
public interface UserService extends IService<User> {

    /**
     * 通过用户名查找菜单权限
     * @param username
     * @return
     */
    List<Menu> menuByName(String username);
}
