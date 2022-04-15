package com.tothefor.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tothefor.mapper.UserRoleMapper;
import com.tothefor.pojo.entity.ItemType;
import com.tothefor.pojo.entity.UserRole;
import com.tothefor.resultR.R;
import com.tothefor.service.impl.UserRoleServiceImpl;
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

/**
 * @Author DragonOne
 * @Date 2022/3/10 16:34
 * @墨水记忆 www.tothefor.com
 */
@RestController
@RequestMapping("/userRole")
public class userRoleController {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    /**
     * @Author DragonOne
     * @Date 2022/3/9 15:44
     * @墨水记忆 www.tothefor.com
     * @方法 itemTypeAll
     * @作用 查询全部类型
     * @参数说明
     * @return
     */
    @GetMapping("/userRoleAll")
    public R<List<UserRole>> userRoleAll(){
        return R.SUCCESS(userRoleMapper.selectList(null));
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
    public R userRoleSave(@RequestBody UserRole item){
        if(userRoleService.saveOrUpdate(item)){
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
    public R userRoleDelete(@PathVariable Integer id){
        if(userRoleMapper.deleteById(id)==1){
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
    public R userRoleBatchDelete(@RequestBody List<Integer> ids){
        int len = ids.size();
        if(userRoleMapper.deleteBatchIds(ids)==len){
            return R.SUCCESS();
        }else {
            return R.FAIL();
        }
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/7 12:37
     * @墨水记忆 www.tothefor.com
     * @方法 PageitemAll
     * @作用 物品的分页查询
     * @参数说明
     * @return
     */
    @GetMapping("/pageAll")
    public R<IPage<UserRole>> PageUserRoleAll(@RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("PageSize") Integer pageSize,
                                              @RequestParam(defaultValue = "") String itemname){

        IPage<UserRole> page = new Page<>(pageNum,pageSize);
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(itemname)){
            queryWrapper.like("rolename",itemname);
        }
        queryWrapper.ne("id",1);
        queryWrapper.orderByDesc("id");
        return R.SUCCESS(userRoleService.page(page, queryWrapper));
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/9 17:13
     * @墨水记忆 www.tothefor.com
     * @方法 export
     * @作用 数据导出
     * @参数说明
     * @return
     */
    @GetMapping("/export")
    public void exportRole(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        String token = request.getHeader("token");

        List<Map<String, Object>> list = CollUtil.newArrayList();
        List<UserRole> all = userRoleMapper.selectList(null);

        for(UserRole it : all){
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("唯一标识符",it.getId());
            row1.put("角色编号",it.getRoleId());
            row1.put("角色名称",it.getRolename());
            row1.put("角色描述",it.getDescription());

            list.add(row1);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("下载信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);


    }

    /**
     * @Author DragonOne
     * @Date 2022/3/9 17:13
     * @墨水记忆 www.tothefor.com
     * @方法 imp
     * @作用 数据导入
     * @参数说明
     * @return
     */
    @PostMapping("/import")
    public R impRole(MultipartFile file) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<UserRole> items = CollUtil.newArrayList();
        try {

            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);

            List<List<Object>> list = reader.read(1);

            System.out.println(list.size());
            for (List<Object> row : list) {
                UserRole item = new UserRole();
                item.setRoleId(Long.valueOf(row.get(0).toString()));
                item.setRolename(row.get(1).toString());
                item.setDescription(row.get(2).toString());

                items.add(item);
            }
        }catch (Exception e){
            return R.FAIL();
        }

        if(userRoleService.saveBatch(items)){
            return R.SUCCESS();
        }else{
            return R.FAIL();
        }
    }


    /**
     * @Author DragonOne
     * @Date 2022/3/10 10:30
     * @墨水记忆 www.tothefor.com
     * @方法 exportMB
     * @作用 导入数据模板
     * @参数说明
     * @return
     */
    @GetMapping("/exportMB")
    public void exportMBRole(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        String token = request.getHeader("token");

        List<Map<String, Object>> list = CollUtil.newArrayList();
        UserRole it = new UserRole();
        it.setRoleId(2L);
        it.setRolename("这里填写角色名称");
        it.setDescription("角色描述");


        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("角色编号",it.getRoleId());
        row1.put("角色名称",it.getRolename());
        row1.put("角色描述",it.getDescription());

        list.add(row1);


        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("导入模板", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);


    }

    /**
     * @Author DragonOne
     * @Date 2022/3/12 17:02
     * @墨水记忆 www.tothefor.com
     * @方法 roleMenu
     * @作用 通过角色id修改菜单权限
     * @参数说明
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public R roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds){
        if(roleId==2) return R.FAIL();
        userRoleService.setRoleMenu(roleId,menuIds);
        return R.SUCCESS();
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/12 17:02
     * @墨水记忆 www.tothefor.com
     * @方法 getroleMenu
     * @作用 通过角色id获取菜单权限
     * @参数说明
     * @return
     */
    @GetMapping("/roleMenu/{roleId}")
    public R getRoleMenu(@PathVariable Integer roleId){
        return R.SUCCESS(userRoleService.getRoleMenu(roleId));
    }


}
