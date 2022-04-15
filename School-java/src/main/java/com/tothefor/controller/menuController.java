package com.tothefor.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tothefor.mapper.MenuMapper;
import com.tothefor.mapper.UserMapper;
import com.tothefor.mapper.UserRoleMapper;
import com.tothefor.pojo.entity.Menu;
import com.tothefor.pojo.entity.User;
import com.tothefor.pojo.entity.UserRole;
import com.tothefor.resultR.R;
import com.tothefor.service.impl.MenuServiceImpl;
import com.tothefor.service.impl.UserRoleServiceImpl;
import com.tothefor.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author DragonOne
 * @Date 2022/3/4 15:31
 * @墨水记忆 www.tothefor.com
 */

@RestController
@RequestMapping("/menu")
public class menuController {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuServiceImpl menuService;


    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:01
     * @墨水记忆 www.tothefor.com
     * @方法 itemAll
     * @作用 查找所有物品信息接口
     * @参数说明
     * @return
     */
    @GetMapping("/menuAll")
    public R menuAll(){
        List<Menu> list = menuService.list();
        List<Menu> parNode = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        for( Menu it : parNode){
            it.setChildren(list.stream().filter(m->it.getId().equals(m.getPid())).collect(Collectors.toList()));
        }

        return R.SUCCESS(parNode);
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:02
     * @墨水记忆 www.tothefor.com
     * @方法 itemSave
     * @作用 插入和修改接口
     * @参数说明
     * @return
     */
    @PostMapping("/save")
    public R menuSave(@RequestBody Menu item){

        if(menuService.saveOrUpdate(item)){
            return R.SUCCESS();
        }else {
            return R.FAIL();
        }
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:06
     * @墨水记忆 www.tothefor.com
     * @方法 itemDelete
     * @作用 根据id删除信息
     * @参数说明
     * @return
     */
    @DeleteMapping("/del/{id}")
    public R menuDelete(@PathVariable Integer id){
        if(menuMapper.deleteById(id)==1){
            return R.SUCCESS();
        }else{
            return R.FAIL();
        }
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:11
     * @墨水记忆 www.tothefor.com
     * @方法 itemBatchDelete
     * @作用 批量删除
     * @参数说明
     * @return
     */
    @PostMapping("/batch/del")
    public R menuBatchDelete(@RequestBody List<Integer> ids){
        int len = ids.size();
        if(menuMapper.deleteBatchIds(ids)==len){
            return R.SUCCESS();
        }else {
            return R.FAIL();
        }
    }

}
